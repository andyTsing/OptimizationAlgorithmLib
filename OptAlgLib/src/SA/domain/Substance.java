package SA.domain;

import java.util.Arrays;

import FA.domain.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * ����
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Substance {
	/**
	 * ԭ��λ��
	 */
	private Position position;
	/**
	 * ��������
	 */
	private double energy;

	public Substance(Position position) {
		super();
		this.position = position;
	}

	public String toString() {
		return "Substance [ " + Arrays.toString(position.getPositionCode()) + ", energy=" + energy + "]";
	}
}
