package device.management.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import device.management.demo.entity.Request;
import device.management.demo.entity.User;
import device.management.demo.entity.response.RequestResponse;
import device.management.demo.service.RequestService;
import device.management.demo.service.UserService;
import device.management.demo.util.requestconst;

@RestController
public class RequestApi {

	@Autowired
	UserService userService;
	@Autowired
	RequestService requestService;

	/**
	 * @summary add new request from user
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param request
	 * @return Request
	 **/
	@PostMapping(path = "/createrequest")
	public ResponseEntity<Object> CreateRequest(@RequestBody RequestResponse request) {
		System.out.println("get request");
		String principalemail = "namnguyen2@gmail.com";
		request.setEmail(principalemail);
		request.setStatus(requestconst.Pending);
		return new ResponseEntity<>(requestService.createRequest(request), HttpStatus.OK);
	}

	/**
	 * @summary show requests of user
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param user
	 * @return listrequest
	 **/
	@GetMapping(path = "/myrequest")
	public ResponseEntity<Object> ViewRequestUser() {
		String principalemail = "namnguyen2@gmail.com";
		User user = userService.getUserByEmail(principalemail);
		List<Request> listRequest = requestService.listRequestbyuser(user);
		if (listRequest.size() == 0) {
			return new ResponseEntity<>(listRequest, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(listRequest, HttpStatus.OK);
	}

	/**
	 * @summary show requests pending
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param
	 * @return listRequestr
	 **/
	@GetMapping(path = "/requestpending")
	public ResponseEntity<Object> viewRequestPending() {
		List<RequestResponse> listRequestr = requestService.listRequestpending();
		if (listRequestr.equals(null)) {
			return new ResponseEntity<>(listRequestr, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(listRequestr, HttpStatus.OK);
	}

	/**
	 * @summary show requests history
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param
	 * @return listRequestr
	 **/
	@GetMapping(path = "/requesthistory")
	public ResponseEntity<Object> viewOldRequest() {
		List<RequestResponse> listRequestr = requestService.listOldRequest();
		if (listRequestr.equals(null)) {
			return new ResponseEntity<>(listRequestr, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(listRequestr, HttpStatus.OK);
	}

	/**
	 * @summary resolve request
	 * @date sep 12, 2018
	 * @author Nam.Nguyen2
	 * @param
	 * @return listRequestr
	 **/
	@PutMapping(path = "/resolve")
	public ResponseEntity<Object> ResolveRequest(@RequestBody RequestResponse request) {
		System.out.println("show:" + request);
		if (request.getStatus().equals(requestconst.Approved)) {
			if (request.getType().equals(requestconst.Update_Info)) {
				userService.updateUser(request);
				RequestResponse rr = requestService.editRequest(request);
				return new ResponseEntity<>(rr, HttpStatus.OK);
			}
		} else if (request.getStatus().equals(requestconst.Reply)) {
			return new ResponseEntity<>(requestService.createRequest(request), HttpStatus.OK);
		} else {
			userService.updateUser(request);
			RequestResponse rr = requestService.editRequest(request);
			return new ResponseEntity<>(rr, HttpStatus.OK);
		}
		return new ResponseEntity<>("ok2", HttpStatus.OK);
	}

}