package com.swan.twoafterfour.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeek.calendar.widget.calendar.entity.CacheDay;
import com.jimmy.common.bean.EventSet;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BaseActivity;
import com.swan.twoafterfour.customclass.BaseFragment;
import com.swan.twoafterfour.globaldata.PostEvent;
import com.swan.twoafterfour.popupwindow.OperationPopupWindow;
import com.swan.twoafterfour.task.eventset.LoadEventSetTask;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends BaseActivity implements View.OnClickListener,
		OnTaskFinishedListener<List<EventSet>> {

	public static int ADD_EVENT_SET_CODE = 1;
	public static String ADD_EVENT_SET_ACTION = "action.add.event.set";

	@BindView(R.id.dlMain)
	DrawerLayout dlMain;
	@BindView(R.id.llTitleDate)
	LinearLayout llTitleDate;
	@BindView(R.id.tvTitleMonth)
	TextView tvTitleMonth;
	@BindView(R.id.tvTitleDay)
	TextView tvTitleDay;
	@BindView(R.id.tvTitle)
	TextView tvTitle;
	@BindView(R.id.tvMenuTitle)
	TextView tvMenuTitle;
	@BindView(R.id.right_menu_ib)
	ImageButton rightMenuIb;
	@BindView(R.id.right_top_tv)
	TextView rightTopTv;
	@BindView(R.id.rvMenuEventSetList)
	RecyclerView rvMenuEventSetList;

	private EventSetAdapter mEventSetAdapter;
	private List<EventSet> mEventSets;

	private BaseFragment mScheduleFragment, mEventSetFragment;
	private EventSet mCurrentEventSet;
	private AddEventSetBroadcastReceiver mAddEventSetBroadcastReceiver;

	private long[] mNotes = new long[2];
	private String[] mMonthText;
	private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;

	public static int operation;

	@Subscribe
	protected void subscribeSetPeriod(Bundle bundle) {
		if (bundle.getInt(PostEvent.TAG) == PostEvent.SET_PERIOD) {
			setPeriodicDayOff();
		}
	}

	private void setPeriodicDayOff() {
		operation = 1;
		rightMenuIb.setVisibility(View.GONE);
		rightTopTv.setVisibility(View.VISIBLE);
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_main;
	}

	@Override
	protected void initial() {
		initUi();
		initEventSetList();
		gotoScheduleFragment();
		initBroadcastReceiver();
		initData();
		EventBus.getDefault().register(this);
	}

	private void initUi() {
		dlMain.setScrimColor(Color.TRANSPARENT);
		mMonthText = getResources().getStringArray(R.array.calendar_month);
		llTitleDate.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.GONE);
		tvTitleMonth.setText(mMonthText[Calendar.getInstance().get(Calendar.MONTH)]);
		tvTitleDay.setText(getString(R.string.calendar_today));
		if (Build.VERSION.SDK_INT < 19) {
			tvMenuTitle.setGravity(Gravity.CENTER_VERTICAL);
		}
	}

	private void initEventSetList() {
		mEventSets = new ArrayList<>();
		LinearLayoutManager manager = new LinearLayoutManager(this);
		manager.setOrientation(LinearLayoutManager.VERTICAL);
		rvMenuEventSetList.setLayoutManager(manager);
		DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
		itemAnimator.setSupportsChangeAnimations(false);
		rvMenuEventSetList.setItemAnimator(itemAnimator);
		mEventSetAdapter = new EventSetAdapter(this, mEventSets);
		rvMenuEventSetList.setAdapter(mEventSetAdapter);
	}

	private void gotoScheduleFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		if (mScheduleFragment == null) {
			mScheduleFragment = ScheduleFragment.getInstance();
			ft.add(R.id.flMainContainer, mScheduleFragment);
		}
		if (mEventSetFragment != null)
			ft.hide(mEventSetFragment);
		ft.show(mScheduleFragment);
		ft.commit();
		llTitleDate.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.GONE);
		dlMain.closeDrawer(Gravity.START);
	}

	private void initBroadcastReceiver() {
		if (mAddEventSetBroadcastReceiver == null) {
			mAddEventSetBroadcastReceiver = new AddEventSetBroadcastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ADD_EVENT_SET_ACTION);
			registerReceiver(mAddEventSetBroadcastReceiver, filter);
		}
	}

	protected void initData() {
		resetMainTitleDate(mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay);
		new LoadEventSetTask(this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void resetMainTitleDate(int year, int month, int day) {
		llTitleDate.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.GONE);
		Calendar calendar = Calendar.getInstance();
		if (year == calendar.get(Calendar.YEAR) &&
				month == calendar.get(Calendar.MONTH) &&
				day == calendar.get(Calendar.DAY_OF_MONTH)) {
			tvTitleMonth.setText(mMonthText[month]);
			tvTitleDay.setText(getString(R.string.calendar_today));
		} else {
			if (year == calendar.get(Calendar.YEAR)) {
				tvTitleMonth.setText(mMonthText[month]);
			} else {
				tvTitleMonth.setText(String.format("%s%s", String.format(getString(R.string
								.calendar_year), year),
						mMonthText[month]));
			}
			tvTitleDay.setText(String.format(getString(R.string.calendar_day), day));
		}
		setCurrentSelectDate(year, month, day);
	}

	private void resetTitleText(String name) {
		llTitleDate.setVisibility(View.GONE);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(name);
	}

	private void setCurrentSelectDate(int year, int month, int day) {
		mCurrentSelectYear = year;
		mCurrentSelectMonth = month;
		mCurrentSelectDay = day;
	}

	protected void recordOperation(int year, int month, int day) {
		switch (operation) {
			case 1:
				new CacheDay(year, month, day, MainActivity.operation).save();
				break;
			default:
				break;
		}
	}

	@OnClick({R.id.ivMainMenu, R.id.llMenuSchedule, R.id.llMenuNoCategory, R.id.tvMenuAddEventSet,
			R.id.right_menu_ib, R.id.right_top_tv})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ivMainMenu:
				dlMain.openDrawer(Gravity.START);
				break;
			case R.id.llMenuSchedule:
				gotoScheduleFragment();
				break;
			case R.id.llMenuNoCategory:
				mCurrentEventSet = new EventSet();
				mCurrentEventSet.setName(getString(R.string.menu_no_category));
				gotoEventSetFragment(mCurrentEventSet);
				break;
			case R.id.tvMenuAddEventSet:
				gotoAddEventSetActivity();
				break;
			case R.id.right_menu_ib:
				showPopupWindow();
				break;
			case R.id.right_top_tv:
				operation = 0;
				DataSupport.deleteAll(CacheDay.class);
				rightTopTv.setVisibility(View.GONE);
				rightMenuIb.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}

	private void showPopupWindow() {
		OperationPopupWindow popupWindow = new OperationPopupWindow(this);
		popupWindow.show();
	}

	public void gotoEventSetFragment(EventSet eventSet) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		if (mCurrentEventSet != eventSet || eventSet.getId() == 0) {
			if (mEventSetFragment != null)
				ft.remove(mEventSetFragment);
			mEventSetFragment = EventSetFragment.getInstance(eventSet);
			ft.add(R.id.flMainContainer, mEventSetFragment);
		}
		ft.hide(mScheduleFragment);
		ft.show(mEventSetFragment);
		ft.commit();
		resetTitleText(eventSet.getName());
		dlMain.closeDrawer(Gravity.START);
		mCurrentEventSet = eventSet;
	}

	private void gotoAddEventSetActivity() {
		startActivityForResult(new Intent(this, AddEventSetActivity.class), ADD_EVENT_SET_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_EVENT_SET_CODE) {
			if (resultCode == AddEventSetActivity.ADD_EVENT_SET_FINISH) {
				EventSet eventSet = (EventSet) data.getSerializableExtra(AddEventSetActivity
						.EVENT_SET_OBJ);
				if (eventSet != null)
					mEventSetAdapter.insertItem(eventSet);
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (dlMain.isDrawerOpen(Gravity.START)) {
			dlMain.closeDrawer(Gravity.START);
		} else {
			System.arraycopy(mNotes, 1, mNotes, 0, mNotes.length - 1);
			mNotes[mNotes.length - 1] = SystemClock.uptimeMillis();
			if (SystemClock.uptimeMillis() - mNotes[0] < 1000) {
				finish();
			} else {
				Toast.makeText(this, getString(R.string.exit_app_hint), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mAddEventSetBroadcastReceiver != null) {
			unregisterReceiver(mAddEventSetBroadcastReceiver);
			mAddEventSetBroadcastReceiver = null;
		}
		if (EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
		super.onDestroy();
	}

	@Override
	public void onTaskFinished(List<EventSet> data) {
		mEventSetAdapter.changeAllData(data);
	}

	private class AddEventSetBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ADD_EVENT_SET_ACTION.equals(intent.getAction())) {
				EventSet eventSet = (EventSet) intent.getSerializableExtra(AddEventSetActivity
						.EVENT_SET_OBJ);
				if (eventSet != null)
					mEventSetAdapter.insertItem(eventSet);
			}
		}
	}

}
