package fifteenpuzzle;

import java.io.Serial;

public class BadBoardException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	public BadBoardException(String message) {
		super(message);
	}
}
