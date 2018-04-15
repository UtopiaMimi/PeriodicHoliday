package com.swan.twoafterfour.versionmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BaseActivity;
import com.swan.twoafterfour.dialog.NewVersionDialog;
import com.swan.twoafterfour.entity.Version;
import com.swan.twoafterfour.netutil.OkHttp3Util;
import com.swan.twoafterfour.netutil.RequestNetCallBack;
import com.swan.twoafterfour.netutil.RequestObjectNetCallBack;
import com.swan.twoafterfour.staticconstant.GlobalData;
import com.swan.twoafterfour.utils.PermissionUtils;
import com.swan.twoafterfour.utils.SharedPreferencesManager;
import com.swan.twoafterfour.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.swan.twoafterfour.utils.ToastUtils.showToast;

/**
 * 版本管理器
 */
public class VersionManager implements View.OnClickListener {
	private final String TAG = "VersionManager";

	private final String REFUSED_VERSION = "refused_version";

	/**
	 * DownloadManager.getDownloadStatus 如果没找到会返回-1
	 */
	private static final int STATUS_UN_FIND = -1;

	public static boolean haveChecked;

	private Context context;
	private BaseActivity Act;

	private Version newVersion;

	private NewVersionDialog newVersionDialog;

	private BroadcastReceiver receiver;

	private DownloadManager downloadManager;

	/**
	 * 忽略“下次再说”（只要有新版本，一定会弹出 Dialog）
	 */
	private boolean ignoreAnotherTime;

	private long id = -1;

	public VersionManager(Context context) {
		this.context = context;
		Act = (BaseActivity) context;
	}

	public VersionManager(Context context, boolean ignoreAnotherTime) {
		this.context = context;
		Act = (BaseActivity) context;
		this.ignoreAnotherTime = ignoreAnotherTime;
	}

	/**
	 * 网络获取服务器最新版本号
	 */
	public void requestRecentCode() {
		haveChecked = true;

		RequestNetCallBack callback = new RequestVersionNetCallBack();
		OkHttp3Util util = OkHttp3Util.getInstance(Act.getBaseContext());
		util.doGet(GlobalData.REMOTE_SERVER_ADDRESS + "api/version/new_version?appSystem=10", new
						HashMap(),
				new HashMap(), callback);
	}

	private void requestFinished() {
		EventBus.getDefault().post(1);
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	private int getVersionCode() {
		int versionCode = 0;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return versionCode;
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public String getVersionName() {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			Log.d(TAG, "info.versionName:" + info.versionName);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 准备下载更新
	 */
	public void prepareUpdate() {
		if (!PermissionUtils.checkStoragePermission()) {
			return;
		}
		// 下载到程序安装目录相对路径，不需要检查文件夹是否存在
//		if (!isFolderExist()) {
//			GlobalMethod.showToast(Act.getBaseContext(), Act.getString(R.string.download_error));
//			return;
//		}

		downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		checkLocalFile();
	}

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver(final long myDownloadReference, final DownloadManager
			downloadManager) {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG, "intent.getAction():" + intent.getAction());
				if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
					String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
					long[] references = intent.getLongArrayExtra(extraID);
					for (long reference : references)
						if (reference == myDownloadReference) {
							UpdaterUtils.downloadDetails(context);
						}
				} else if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
					long downloadId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, -1);
					if (myDownloadReference == downloadId) {
						Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
						Uri realUri = getApkFilePathUri(uri, downloadId);
						Log.d(TAG, "uri:" + uri);
						Log.d(TAG, "realUri:" + realUri);
						UpdaterUtils.install(context, realUri);
					}
					context.unregisterReceiver(this);
					receiver = null;
				}
			}
		};
		IntentFilter clickFilter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		context.registerReceiver(receiver, clickFilter);
		IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		context.registerReceiver(receiver, completeFilter);
	}

	private class RequestVersionNetCallBack extends RequestObjectNetCallBack<Version> {

		@Override
		protected void onFailed(int errorCode, String message) {
			showFailingMessage(context.getString(R.string.connect_exception));
		}

		@Override
		protected void onSuccessful(Version data) {
			/*
			{"Data":{"Id":"Uzgo","VersionCode":"1.3.0.0B13",
			"Url":"/f/1708/02/86f22ce259644d8194d7c026c7994d15/winsharedroom.apk","AppSystem":10,
			"Iteration":["1更新我的空间功能","2更多空间更新"],"IsMust":false,"Enabled":true,
			"CreateTime":"2017-08-02 17:41:42","ModifyTime":"2017-08-03 09:36:55"},"Code":0,
			"Message":"","successful":true}
			 */
			requestSuccessful(data);
		}

		/**
		 * 请求失败提示
		 *
		 * @param message 错误信息
		 */
		private void showFailingMessage(final String message) {
			Act.runOnUiThread(() -> {
				requestFinished();
				if (ignoreAnotherTime)
					showToast(Act, message);
			});
		}

		/**
		 * 请求成功
		 *
		 * @param version 最新版本
		 */
		private void requestSuccessful(final Version version) {
			Act.runOnUiThread(() -> {
				requestFinished();
				checkVersionCode(version);
			});
		}

	}

	/**
	 * 检查版本号
	 *
	 * @param version 版本号
	 */
	private void checkVersionCode(Version version) {
		int recentVersionCode = Integer.valueOf(version.getVersionCode());
		int currentVersionCode = getVersionCode();
		Log.d(TAG, "recentVersionCode:" + recentVersionCode);
		Log.d(TAG, "currentVersionCode:" + currentVersionCode);
		if (recentVersionCode > currentVersionCode) {
			haveNewVersion(version);
		} else {
			if (ignoreAnotherTime)
				ToastUtils.showToast(Act, context.getString(R.string.about_us_newest_version));
		}
	}

	/**
	 * 有新版本
	 *
	 * @param version 版本号
	 */
	private void haveNewVersion(Version version) {
		if (version.isEnabled()) {
			newVersion = version;
			int refusedVersion = SharedPreferencesManager.getInt(context, REFUSED_VERSION, 0);
			if (!version.isIsMust() && !ignoreAnotherTime && Integer.valueOf(version
					.getVersionCode()) == refusedVersion) {
				return;
			}
			showNewVersionDialog(version.getIteration());
		}
	}

	/**
	 * 弹出对话框
	 *
	 * @param iterations 新版本介绍
	 */
	private void showNewVersionDialog(List<String> iterations) {
		// Dialog 需要承载在 Activity 之上
		NewVersionDialog.Builder newVersionDialogBuilder = new NewVersionDialog.Builder(Act);
		newVersionDialog = newVersionDialogBuilder.create();
		// 不能点击外部 cancel，可以点击返回键取消
		newVersionDialog.setCanceledOnTouchOutside(false);
		newVersionDialog.setOnCancelListener(new DialogOnCancelListener());

		StringBuffer introduction = new StringBuffer(Act.getString(R.string.introduction_title));
		for (int i = 0; i < iterations.size(); i++) {
			introduction.append("\n").append(iterations.get(i));
		}
		newVersionDialog.introductionTv.setText(introduction);

		if (newVersion.isIsMust()) {
			newVersionDialog.oneOptionUpdateLl.setVisibility(View.VISIBLE);
			newVersionDialog.oneOptionUpdateLl.setOnClickListener(this);
		} else {
			newVersionDialog.twoOptionLl.setVisibility(View.VISIBLE);
			newVersionDialog.anotherTimeTv.setOnClickListener(this);
			newVersionDialog.updateNowTv.setOnClickListener(this);
		}

		newVersionDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialog_new_version_two_option_another_time_tv:
				newVersionDialog.dismiss();
				anotherTime();
				break;
			case R.id.dialog_new_version_one_option_update_now_ll:
			case R.id.dialog_new_version_two_option_update_now_tv:
				updateNow();
				break;
			case R.id.dialog_new_version_one_option_cancel_ll:
				cancelUpdate();
				break;
			default:
				break;
		}
	}

	/**
	 * 下次再说
	 */
	private void anotherTime() {
		SharedPreferencesManager.putInt(context, REFUSED_VERSION,
				Integer.valueOf(newVersion.getVersionCode()));
	}

	/**
	 * 立即更新
	 */
	private void updateNow() {
		if (!newVersion.isIsMust()) {
			newVersionDialog.dismiss();
		} else {
			newVersionDialog.introductionLl.setVisibility(View.GONE);
			newVersionDialog.oneOptionUpdateLl.setVisibility(View.GONE);
			newVersionDialog.updatingTv.setVisibility(View.VISIBLE);
			newVersionDialog.oneOptionCancelLl.setVisibility(View.VISIBLE);
			newVersionDialog.oneOptionCancelLl.setOnClickListener(this);
		}
		prepareUpdate();
	}

	/**
	 * 取消更新
	 */
	private void cancelUpdate() {
		// 魅族，下载完成后，取消安装，取消更新，无法删除文件
		downloadManager.remove(id);
		Log.d(TAG, "receiver:" + receiver);
		if (receiver != null) {
			context.unregisterReceiver(receiver);
		}
		newVersionDialog.dismiss();
		Act.finish();
	}

	/**
	 * 对话框消失监听器
	 * 已禁止点击外部 cancel 对话框，只有返回键会 cancel
	 */
	private class DialogOnCancelListener implements DialogInterface.OnCancelListener {

		@Override
		public void onCancel(DialogInterface dialog) {
			if (newVersion.isIsMust()) {
				cancelUpdate();
			} else {
				anotherTime();
			}
		}

	}

	/**
	 * 转化contentUri为fileUri
	 *
	 * @param contentUri 包含 content 的 Uri
	 * @param downLoadId 下载方法返回系统为当前下载请求分配的一个唯一的 ID
	 * @return fileUri
	 */
	private Uri getApkFilePathUri(Uri contentUri, long downLoadId) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downLoadId);
		Cursor c = downloadManager.query(query);
		if (c.moveToFirst()) {
			int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
			// 下载失败也会返回这个广播，所以要判断下是否真的下载成功
			if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
				// 获取下载好的 apk 路径
				String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager
						.COLUMN_LOCAL_URI));
				if (downloadFileLocalUri != null) {
					File mFile = new File(Uri.parse(downloadFileLocalUri).getPath());
					String uriString = mFile.getAbsolutePath();
					// 提示用户安装
					contentUri = Uri.parse("file://" + uriString);
				}
			}
		}
		return contentUri;
	}

	/**
	 * 检查本地文件
	 */
	private void checkLocalFile() {
		if (!UpdaterUtils.checkDownloadState(context)) {
			// TODO: 2017/8/16 下载服务不可用
			UpdaterUtils.showDownloadSetting(context);
			return;
		}

		long downloadId = UpdaterUtils.getLocalDownloadId(context);
		Log.d(TAG, "local download id is " + downloadId);
		if (downloadId != -1L) {
			//获取下载状态
			int status = getDownloadStatus(downloadId);
			Log.d(TAG, "status:" + status);
			switch (status) {
				//下载成功
				case DownloadManager.STATUS_SUCCESSFUL:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = STATUS_SUCCESSFUL");
					Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
					Uri realUri = getApkFilePathUri(uri, downloadId);
					if (uri != null) {
						// 如果 apk 的版本号大于当前应用的版本号，并且等于服务器最新版版本号，直接安装
						if (UpdaterUtils.compare(context, realUri.getPath(), Integer.valueOf
								(newVersion.getVersionCode()))) {
							UpdaterUtils.install(context, realUri);
							return;
						} else {
							//从F DownloadManager 中移除这个任务
							downloadManager.remove(downloadId);
						}
					}
					//重新下载
					update();
					break;
				//下载失败
				case DownloadManager.STATUS_FAILED:
					Log.d(TAG, "download failed " + downloadId);
					update();
					break;
				case DownloadManager.STATUS_RUNNING:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = STATUS_RUNNING");
					break;
				case DownloadManager.STATUS_PENDING:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = STATUS_PENDING");
					break;
				case DownloadManager.STATUS_PAUSED:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = STATUS_PAUSED");
					break;
				case STATUS_UN_FIND:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = STATUS_UN_FIND");
					update();
					break;
				default:
					Log.d(TAG, "downloadId=" + downloadId + " ,status = " + status);
					break;
			}
		} else {
			update();
		}
	}

	/**
	 * 获取下载状态
	 *
	 * @param downloadId an ID for the download, unique across the system.
	 *                   This ID is used to make future calls related to this download.
	 * @return int
	 * @see DownloadManager#STATUS_PENDING
	 * @see DownloadManager#STATUS_PAUSED
	 * @see DownloadManager#STATUS_RUNNING
	 * @see DownloadManager#STATUS_SUCCESSFUL
	 * @see DownloadManager#STATUS_FAILED
	 */
	private int getDownloadStatus(long downloadId) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		Cursor c = downloadManager.query(query);
		if (c != null) {
			try {
				if (c.moveToFirst()) {
					return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
				}
			} finally {
				c.close();
			}
		}
		return -1;
	}

	/**
	 * 更新
	 */
	private void update() {
		id = startDownload();
		Log.i(TAG, "id:" + id);
		//把DownloadId保存到本地
		UpdaterUtils.saveDownloadId(context, id);
		registerReceiver(id, downloadManager);
	}

	/**
	 * 开始下载
	 *
	 * @return 下载任务的 id
	 */
	private long startDownload() {
		UpdaterConfig updaterConfig = new UpdaterConfig.Builder()
				.setTitle(context.getString(R.string.app_name))
				.setDescription(context.getString(R.string.version_update))
				.setFileUrl(GlobalData.DOWNLOAD_SERVER_ADDRESS + newVersion.getUrl())
				.setCanMediaScanner(true)
				.build();

		// TODO: 2017/8/3 github issue 为什么只有在WIFI情况下才能下载
		// 【在android 7.0小米4 机器上，会报错。在android6.0上乐视1S,也会报错。但在android5.1及以下机器上就没有这个问题】
		// TODO: 2017/8/3 github issue 7.0以上需要Intent.FLAG_GRANT_READ_URI_PERMISSION 权限？
		// TODO: 2017/8/3 实现上层设置文件的下载路径

		DownloadManager.Request req = new DownloadManager.Request(Uri.parse(updaterConfig
				.getFileUrl()));
		req.setAllowedNetworkTypes(updaterConfig.getAllowedNetworkTypes());
		//req.setAllowedOverMetered()
		//移动网络是否允许下载
		req.setAllowedOverRoaming(updaterConfig.isAllowedOverRoaming());

		if (updaterConfig.isCanMediaScanner()) {
			//能够被MediaScanner扫描
			req.allowScanningByMediaScanner();
		}

		//是否显示状态栏下载UI
		req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		//点击正在下载的Notification进入下载详情界面，如果设为true则可以看到下载任务的进度，如果设为false，则看不到我们下载的任务
		req.setVisibleInDownloadsUi(updaterConfig.isShowDownloadUI());

		//设置文件的保存的位置[三种方式]
		//第一种
		//file:///storage/emulated/0/Android/data/your-package/files/WinSharedRoom.apk
		req.setDestinationInExternalFilesDir(context, "", "WinSharedRoom.apk");
		//第二种
		//file:///storage/emulated/0/Download/prepareUpdate.apk
//		req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "WinSharedRoom
// .apk");
		////第三种 自定义文件路径
//		req.setDestinationUri();


		// 设置一些基本显示信息
		req.setTitle(updaterConfig.getTitle());
		req.setDescription(updaterConfig.getDescription());

		return downloadManager.enqueue(req);
	}

}
