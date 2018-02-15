package com.swan.twoafterfour.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;
import com.jimmy.common.bean.EventSet;
import com.jimmy.common.bean.Schedule;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.jimmy.common.util.DeviceUtils;
import com.jimmy.common.util.ToastUtils;
import com.swan.twoafterfour.R;
import com.swan.twoafterfour.customclass.BaseFragment;
import com.swan.twoafterfour.dialog.SelectDateDialog;
import com.swan.twoafterfour.task.eventset.GetScheduleTask;
import com.swan.twoafterfour.task.schedule.AddScheduleTask;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class EventSetFragment extends BaseFragment implements View.OnClickListener,
		OnTaskFinishedListener<List<Schedule>>, SelectDateDialog.OnSelectDateListener {

	public static String EVENT_SET_OBJ = "event.set.obj";

	@BindView(R.id.rvScheduleList)
	ScheduleRecyclerView rvScheduleList;
	@BindView(R.id.etInputContent)
	EditText etInputContent;
	@BindView(R.id.rlNoTask)
	RelativeLayout rlNoTask;
	private SelectDateDialog mSelectDateDialog;

	private ScheduleAdapter mScheduleAdapter;
	private EventSet mEventSet;

	private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
	private int mPosition = -1;
	private long mTime;

	public static EventSetFragment getInstance(EventSet eventSet) {
		EventSetFragment fragment = new EventSetFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(EVENT_SET_OBJ, eventSet);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void initBottomInputBar() {
		etInputContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				etInputContent.setGravity(s.length() == 0 ? Gravity.CENTER : Gravity
						.CENTER_VERTICAL);
			}
		});
		etInputContent.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
		});
	}

	@Override
	protected void initData() {
		mEventSet = (EventSet) getArguments().getSerializable(EVENT_SET_OBJ);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_event_set;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		initBottomInputBar();
		initScheduleList();
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void requestData() {

	}

	@Override
	public void onResume() {
		super.onResume();
		new GetScheduleTask(getCurrentActivity(), this, mEventSet.getId()).executeOnExecutor
				(AsyncTask
				.THREAD_POOL_EXECUTOR);
	}

	private void initScheduleList() {
		LinearLayoutManager manager = new LinearLayoutManager(getCurrentActivity());
		manager.setOrientation(LinearLayoutManager.VERTICAL);
		rvScheduleList.setLayoutManager(manager);
		DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
		itemAnimator.setSupportsChangeAnimations(false);
		rvScheduleList.setItemAnimator(itemAnimator);
		mScheduleAdapter = new ScheduleAdapter(getCurrentActivity(), this);
		rvScheduleList.setAdapter(mScheduleAdapter);
	}

	@OnClick({R.id.ibMainClock, R.id.ibMainOk})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ibMainClock:
				showSelectDateDialog();
				break;
			case R.id.ibMainOk:
				addSchedule();
				break;
		}
	}

	private void showSelectDateDialog() {
		if (mSelectDateDialog == null) {
			Calendar calendar = Calendar.getInstance();
			mSelectDateDialog = new SelectDateDialog(getCurrentActivity(), this, calendar.get
					(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mPosition);
		}
		mSelectDateDialog.show();
	}

	private void closeSoftInput() {
		etInputContent.clearFocus();
		DeviceUtils.closeSoftInput(getCurrentActivity(), etInputContent);
	}

	private void addSchedule() {
		String content = etInputContent.getText().toString();
		if (TextUtils.isEmpty(content)) {
			ToastUtils.showShortToast(getCurrentActivity(), R.string.schedule_input_content_is_no_null);
		} else {
			closeSoftInput();
			Schedule schedule = new Schedule();
			schedule.setTitle(content);
			schedule.setState(0);
			schedule.setColor(mEventSet.getColor());
			schedule.setEventSetId(mEventSet.getId());
			schedule.setTime(mTime);
			schedule.setYear(mCurrentSelectYear);
			schedule.setMonth(mCurrentSelectMonth);
			schedule.setDay(mCurrentSelectDay);
			new AddScheduleTask(getCurrentActivity(), new OnTaskFinishedListener<Schedule>() {
				@Override
				public void onTaskFinished(Schedule data) {
					if (data != null) {
						mScheduleAdapter.insertItem(data);
						etInputContent.getText().clear();
						rlNoTask.setVisibility(View.GONE);
						mTime = 0;
					}
				}
			}, schedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	private void setCurrentSelectDate(int year, int month, int day) {
		mCurrentSelectYear = year;
		mCurrentSelectMonth = month;
		mCurrentSelectDay = day;
	}

	@Override
	public void onTaskFinished(List<Schedule> data) {
		mScheduleAdapter.changeAllData(data);
		rlNoTask.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
	}


	@Override
	public void onSelectDate(int year, int month, int day, long time, int position) {
		setCurrentSelectDate(year, month, day);
		mTime = time;
		mPosition = position;
	}
}
