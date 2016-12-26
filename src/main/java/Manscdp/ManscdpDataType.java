package Manscdp;

public class ManscdpDataType {

	public class StatusType {
		public final static String ON = "ON";
		public final static String OFF = "OFF";
	}

	public class ResultType {
		public final static String OK = "OK";
		public final static String ERROR = "ERROR";
	}

	public class RecordType {
		public final static String Record = "Record";
		public final static String StopRecord = "StopRecord";
	}

	public class GuardType {
		public final static String SetGuard = "SetGuard";
		public final static String ResetGuard = "ResetGuard";
	}

	public static class ItemType {
		// 设备/区域/系统编码
		String deviceID;
		// 设备/区域/系统名称
		String name;
		// 当为设备时，设备厂商
		String manufacture;
		String ResType;
		String RecLocation;
		String OperateType;
		// 当为设备时，设备型号
		String model;
		// 当为设备时，设备归属
		String owner;
		// 行政区域
		String civilCode;
		// 警区
		String block;
		// 当为设备时，安装地址
		String address;
		// 当为设备时，是否有子设备
		int parental;
		// 父设备/区域/系统ID
		String parentID;
		// 信令安全模式
		int safetyWay = 0;
		// 注册方式
		int registerWay = 1;
		// 证书序列号
		String certNum;
		// 证书有效标识
		int certifiable = 0;
		// 无效原因码
		int errCode;
		// 证书终止有效期
		String endTime;
		// 保密属性
		int secrecy = 0;
		// IP地址
		String ipAddress;
		String Port;
		// 设备口令
		String password;
		// 设备状态
		String status = StatusType.ON;
		// 经度
		double longitude;
		// 维度
		double latitude;
		String PlayUrl;

		public String getdeviceID() {
			return deviceID;
		}

		public void setdeviceID(String deviceID) {
			this.deviceID = deviceID;
		}

		public String getname() {
			return name;
		}

		public void setname(String name) {
			this.name = name;
		}

		public String getmanufacture() {
			return manufacture;
		}

		public void setmanufacture(String manufacture) {
			this.manufacture = manufacture;
		}

		public String getResType() {
			return ResType;
		}

		public void setResType(String ResType) {
			this.ResType = ResType;
		}

		public String getRecLocation() {
			return RecLocation;
		}

		public void setRecLocation(String RecLocation) {
			this.RecLocation = RecLocation;
		}

		public String getOperateType() {
			return OperateType;
		}

		public void setOperateType(String OperateType) {
			this.OperateType = OperateType;
		}

		public String getmodel() {
			return model;
		}

		public void setmodel(String model) {
			this.model = model;
		}

		public String getowner() {
			return owner;
		}

		public void setowner(String owner) {
			this.owner = owner;
		}

		public String getcivilCode() {
			return civilCode;
		}

		public void setcivilCode(String civilCode) {
			this.civilCode = civilCode;
		}

		public String getblock() {
			return block;
		}

		public void setblock(String block) {
			this.block = block;
		}

		public String getaddress() {
			return address;
		}

		public void setaddress(String address) {
			this.address = address;
		}

		public int getparental() {
			return parental;
		}

		public void setparental(int parental) {
			this.parental = parental;
		}

		public String getparentID() {
			return parentID;
		}

		public void setparentID(String parentID) {
			this.parentID = parentID;
		}

		public String getcertNum() {
			return certNum;
		}

		public void setcertNum(String certNum) {
			this.certNum = certNum;
		}

		public int geterrCode() {
			return errCode;
		}

		public void seterrCode(int errCode) {
			this.errCode = errCode;
		}

		public String getendTime() {
			return endTime;
		}

		public void setendTime(String endTime) {
			this.endTime = endTime;
		}

		public String getipAddress() {
			return ipAddress;
		}

		public void setipAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getPort() {
			return Port;
		}

		public void setPort(String Port) {
			this.Port = Port;
		}

		public String getpassword() {
			return password;
		}

		public void setpassword(String password) {
			this.password = password;
		}

		public double getlongitude() {
			return longitude;
		}

		public void setlongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getlatitude() {
			return latitude;
		}

		public void setlatitude(double latitude) {
			this.latitude = latitude;
		}

		public String getPlayUrl() {
			return PlayUrl;
		}

		public void setPlayUrl(String PlayUrl) {
			this.PlayUrl = PlayUrl;
		}
	}

	public static class ItemFileType {
		// 设备/区域/系统编码
		String deviceID;
		// 设备/区域/系统名称
		String name;
		// 文件路径名
		String filePath;
		// 录像地址
		String address;
		// 录像开始时间
		String startTime;
		// 录像结束时间
		String endTime;
		// 保密属性
		int secrecy = 0;
		// 录像产生类型
		String type;
		// 录像触发者ID
		String recorderID;
		String size;
		String playurl;

		public String getdeviceID() {
			return deviceID;
		}

		public void setdeviceID(String deviceID) {
			this.deviceID = deviceID;
		}

		public String getname() {
			return name;
		}

		public void setname(String name) {
			this.name = name;
		}

		public String getfilePath() {
			return filePath;
		}

		public void setfilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getaddress() {
			return address;
		}

		public void setaddress(String address) {
			this.address = address;
		}

		public String getstartTime() {
			return startTime;
		}

		public void setstartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getendTime() {
			return endTime;
		}

		public void setendTime(String endTime) {
			this.endTime = endTime;
		}

		public String gettype() {
			return type;
		}

		public void settype(String type) {
			this.type = type;
		}

		public String getrecorderID() {
			return recorderID;
		}

		public void setrecorderID(String recorderID) {
			this.recorderID = recorderID;
		}

		public String getsize() {
			return size;
		}

		public void setsize(String size) {
			this.size = size;
		}

		public String getplayurl() {
			return playurl;
		}

		public void setplayurl(String playurl) {
			this.playurl = playurl;
		}
	}

	public static class AlarmStatusItem {
		// 报警设备编码
		String deviceID;
		// 报警设备状态
		String status;

		public String getdeviceID() {
			return deviceID;
		}

		public void setdeviceID(String deviceID) {
			this.deviceID = deviceID;
		}

		public String getstatus() {
			return status;
		}

		public void setstatus(String status) {
			this.status = status;
		}
	}
}
