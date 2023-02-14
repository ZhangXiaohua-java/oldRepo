package cloud.huel.spike.ex;

/**
 * @author 张晓华
 * @date 2022-9-8
 */
public class OrderException extends RuntimeException{


	public OrderException() {
	}

	public OrderException(String message) {
		super(message);
	}
}
