package com.swan.twoafterfour.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by TongYuliang on 2017/8/8.
 */

public class Version implements Parcelable {
	/**
	 * Id : sample string 1
	 * VersionCode : sample string 2
	 * Url : sample string 3
	 * AppSystem : 4
	 * Iteration : ["sample string 1","sample string 2"]
	 * IsMust : true
	 * Enabled : true
	 * CreateTime : 2017-08-08 10:57:51
	 * ModifyTime : 2017-08-08 10:57:51
	 */

	private String Id;
	private String VersionCode;
	private String Url;
	private int AppSystem;
	private boolean IsMust;
	private boolean Enabled;
	private String CreateTime;
	private String ModifyTime;
	private List<String> Iteration;

	protected Version(Parcel in) {
		Id = in.readString();
		VersionCode = in.readString();
		Url = in.readString();
		AppSystem = in.readInt();
		IsMust = in.readByte() != 0;
		Enabled = in.readByte() != 0;
		CreateTime = in.readString();
		ModifyTime = in.readString();
		Iteration = in.createStringArrayList();
	}

	public static final Creator<Version> CREATOR = new Creator<Version>() {
		@Override
		public Version createFromParcel(Parcel in) {
			return new Version(in);
		}

		@Override
		public Version[] newArray(int size) {
			return new Version[size];
		}
	};

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getVersionCode() {
		return VersionCode;
	}

	public void setVersionCode(String VersionCode) {
		this.VersionCode = VersionCode;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String Url) {
		this.Url = Url;
	}

	public int getAppSystem() {
		return AppSystem;
	}

	public void setAppSystem(int AppSystem) {
		this.AppSystem = AppSystem;
	}

	public boolean isIsMust() {
		return IsMust;
	}

	public void setIsMust(boolean IsMust) {
		this.IsMust = IsMust;
	}

	public boolean isEnabled() {
		return Enabled;
	}

	public void setEnabled(boolean Enabled) {
		this.Enabled = Enabled;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String CreateTime) {
		this.CreateTime = CreateTime;
	}

	public String getModifyTime() {
		return ModifyTime;
	}

	public void setModifyTime(String ModifyTime) {
		this.ModifyTime = ModifyTime;
	}

	public List<String> getIteration() {
		return Iteration;
	}

	public void setIteration(List<String> Iteration) {
		this.Iteration = Iteration;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Id);
		dest.writeString(VersionCode);
		dest.writeString(Url);
		dest.writeInt(AppSystem);
		dest.writeByte((byte) (IsMust ? 1 : 0));
		dest.writeByte((byte) (Enabled ? 1 : 0));
		dest.writeString(CreateTime);
		dest.writeString(ModifyTime);
		dest.writeStringList(Iteration);
	}
}
