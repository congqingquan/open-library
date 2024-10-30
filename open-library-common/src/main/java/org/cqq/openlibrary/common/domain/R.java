package org.cqq.openlibrary.common.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Response entity
 *
 * @author Qingquan
 */
@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
public class R<T> {
	
	private Integer code;

	private T data;

	private String message;

	public R() {}
	
	public R(ROption option) {
		this.code = option.getCode();
		this.message = option.getMessage();
	}

	public R(Integer code, T data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}
	
	public static R<?> success() {
		return success(null);
	}
	
	public static <T> R<T> success(T data) {
		return new R<>(ROption.SUCCESS.getCode(), data, ROption.SUCCESS.getMessage());
	}
	
	public static <T> R<T> response(Integer code, T data, String message) {
		return new R<>(code, data, message);
	}
	
	public static <T> R<T> response(ROption option) {
		return new R<>(option.getCode(), null, option.getMessage());
	}
}