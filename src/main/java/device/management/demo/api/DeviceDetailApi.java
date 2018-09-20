package device.management.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import device.management.demo.entity.Device;
import device.management.demo.entity.DeviceDetail;
import device.management.demo.entity.Device_Deliver_Receive;
import device.management.demo.entity.dto.DetailDTO;
import device.management.demo.entity.dto.FilterDetailDTO;
import device.management.demo.entity.response.DetailResponse;
import device.management.demo.service.DeviceDetailService;
import device.management.demo.service.DeviceService;
import device.management.demo.service.Device_Deliver_ReceiveService;
import device.management.demo.util.detailConst;

@RestController
public class DeviceDetailApi {

	@Autowired
	DeviceDetailService deviceDetailService;

	@Autowired
	Device_Deliver_ReceiveService device_Deliver_ReceiveService;
	@Autowired
	DeviceService deviceService;

	/**
	 * @summary get list devicedetails
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param  Device
	 * @return List<DeviceDetail> deviceDetail
	 **/
	@PostMapping(path = "/Getdevicedetails")
	public ResponseEntity<Object> GetDeviceDetails(@RequestBody Device device) {
		List<DetailResponse> deviceDetail = deviceDetailService.getDeviceDetails(device);
		System.out.println("show devicedetails:" + deviceDetail);
		return new ResponseEntity<>(deviceDetail, HttpStatus.OK);
	}
	
	/**
	 * @summary edit devicedetails
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param  Device
	 * @return List<DeviceDetail> deviceDetail
	 **/
	@PutMapping(path = "/editdevicedetails")
	public ResponseEntity<Object> editDeviceDetails(@RequestBody DeviceDetail deviceDetail) {
		System.out.println("show devicedetails:" + deviceDetail);
		return new ResponseEntity<>(deviceDetailService.editDeviceDetails(deviceDetail), HttpStatus.OK);
	}

	/**
	 * @summary del devicedetails
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param  Device
	 * @return String message
	 **/
	// only delete device if record in device_Deliver_Receive have field date return
	// not null validation return field form font end
	@DeleteMapping(path = "/deldevicedetails")
	public ResponseEntity<Object> delDeviceDetails(@RequestBody List<DeviceDetail> deviceDetail) {

		for (DeviceDetail devObj : deviceDetail) {
			Device_Deliver_Receive devDeReObj = device_Deliver_ReceiveService.getDevDeRe(devObj);;
			deviceDetailService.DelDevDetailById(devObj.getId());
		}
//		deviceDetailService.DelDevDetails(deviceDetail);
		return new ResponseEntity<>("delete Device Successed", HttpStatus.OK);
	}

	/**
	 * @summary filter devicedetails not used and normal for allocation via name or catalog
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param  Device
	 * @return String message
	 **/
	// filter devicedetails not used and normal for allocation
	@PostMapping(path = "/filterdetailsnotused")
	public ResponseEntity<Object> GetDetailsNotUsed(@RequestBody FilterDetailDTO filter) {
		List<DetailResponse> deviceDetail = deviceDetailService.filterDetails(detailConst.NOTUSED, detailConst.NORMAL,
				filter.getName(), filter.getCatalog());
		System.out.println("show devicedetails:" + deviceDetail);
		return new ResponseEntity<>(deviceDetail, HttpStatus.OK);
	}
	
	/**
	 * @summary add new device detail
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param  DetailDTO d
	 * @return DetailObj
	 **/
	// add devicedetails
	@PostMapping(path = "/adddetail")
	public ResponseEntity<Object> AddDetail(@RequestBody DetailDTO d) {
		System.out.println("show:" + d);
		DeviceDetail DetailObj = deviceDetailService.addDeviceDetail(d);
		return new ResponseEntity<>(DetailObj, HttpStatus.OK);
	}

}