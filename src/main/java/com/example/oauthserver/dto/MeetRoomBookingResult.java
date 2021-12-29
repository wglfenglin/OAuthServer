package com.example.oauthserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author yls
 * @date 2019/4/15 17:26 通用接口返回
 */
@ToString
@Getter
@Setter
public class MeetRoomBookingResult {

	/**
	 * 返回状态 200成功 500异常
	 */
	private Integer state;
	/**
	 * 返回信息 example:成功/失败
	 */
	private String info;
	/**
	 * 返回数据
	 */
	private Object ob;

	public MeetRoomBookingResult(HttpStatus state) {
		this.state = state.value();
	}

	public MeetRoomBookingResult(HttpStatus state, String info, Object ob) {
		this.state = state.value();
		this.info = info;
		this.ob = ob;
	}

	public MeetRoomBookingResult(HttpStatus state, String info) {
		this.state = state.value();
		this.info = info;
	}


	public static MeetRoomBookingResult success() {
		return new MeetRoomBookingResult(HttpStatus.OK, "成功");
	}

	public static MeetRoomBookingResult success(Object ob) {
		return new MeetRoomBookingResult(HttpStatus.OK, "成功", ob);
	}

	public static MeetRoomBookingResult fail() {
		return new MeetRoomBookingResult(HttpStatus.INTERNAL_SERVER_ERROR, "失败");
	}

	public static MeetRoomBookingResult fail(String info) {
		return new MeetRoomBookingResult(HttpStatus.INTERNAL_SERVER_ERROR, info);
	}
}
