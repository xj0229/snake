package Manscdp;

import java.util.List;

import Manscdp.ManscdpDataType.ItemType;

public class CatalogQueryResParam {
	// 目标设备编码
	String deviceID;
	// 命令序列号
	Integer sn;
	// 查询结果总数
	int sumNum;
	// 设备目录项列表
	List<ItemType> devList;
	// 扩展信息
	String[] info;

	public String getdeviceID() {
		return deviceID;
	}

	public void setdeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public int getsn() {
		return sn;
	}

	public void setsn(int sn) {
		this.sn = sn;
	}

	public int getsumNum() {
		return sumNum;
	}

	public void setsumNum(int sumNum) {
		this.sumNum = sumNum;
	}

	public List<ItemType> getdevList() {
		return devList;
	}

	public void setdevList(String DeviceID, String Name, String Manufacturer, String ResType, String RecLocation,
			String OperateType, String Model, String Block, String Address, int Parental, String ParentID,
			String CertNum, int ErrCode, String EndTime, String IPAddress, String Port, String Password,
			double Longitude, double Latitude, String PlayUrl, int i, List<ItemType> devlist) {
		ItemType itemType = new ItemType();
		itemType.setdeviceID(DeviceID);
		itemType.setname(Name);
		itemType.setmanufacture(Manufacturer);
		itemType.setResType(ResType);
		itemType.setRecLocation(RecLocation);
		itemType.setOperateType(OperateType);
		itemType.setmodel(Model);
		itemType.setblock(Block);
		itemType.setaddress(Address);
		itemType.setparental(Parental);
		itemType.setparentID(ParentID);
		itemType.setcertNum(CertNum);
		itemType.seterrCode(ErrCode);
		itemType.setendTime(EndTime);
		itemType.setipAddress(IPAddress);
		itemType.setPort(Port);
		itemType.setpassword(Password);
		itemType.setlongitude(Longitude);
		itemType.setlatitude(Latitude);
		itemType.setPlayUrl(PlayUrl);
		devlist.add(itemType);
		this.devList = devlist;
	}
	// public String[] getinfo(){
	// return info;
	// }
	// public void setinfo(String[] info) {
	// this.info = info;
	// }
}
