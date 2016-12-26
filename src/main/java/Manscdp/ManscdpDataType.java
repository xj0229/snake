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
		// �豸/����/ϵͳ����
		String deviceID;
		// �豸/����/ϵͳ����
		String name;
		// ��Ϊ�豸ʱ���豸����
		String manufacture;
		String ResType;
		String RecLocation;
		String OperateType;
		// ��Ϊ�豸ʱ���豸�ͺ�
		String model;
		// ��Ϊ�豸ʱ���豸����
		String owner;
		// ��������
		String civilCode;
		// ����
		String block;
		// ��Ϊ�豸ʱ����װ��ַ
		String address;
		// ��Ϊ�豸ʱ���Ƿ������豸
		int parental;
		// ���豸/����/ϵͳID
		String parentID;
		// ���ȫģʽ
		int safetyWay = 0;
		// ע�᷽ʽ
		int registerWay = 1;
		// ֤�����к�
		String certNum;
		// ֤����Ч��ʶ
		int certifiable = 0;
		// ��Чԭ����
		int errCode;
		// ֤����ֹ��Ч��
		String endTime;
		// ��������
		int secrecy = 0;
		// IP��ַ
		String ipAddress;
		String Port;
		// �豸����
		String password;
		// �豸״̬
		String status = StatusType.ON;
		// ����
		double longitude;
		// ά��
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
		// �豸/����/ϵͳ����
		String deviceID;
		// �豸/����/ϵͳ����
		String name;
		// �ļ�·����
		String filePath;
		// ¼���ַ
		String address;
		// ¼��ʼʱ��
		String startTime;
		// ¼�����ʱ��
		String endTime;
		// ��������
		int secrecy = 0;
		// ¼���������
		String type;
		// ¼�񴥷���ID
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
		// �����豸����
		String deviceID;
		// �����豸״̬
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
