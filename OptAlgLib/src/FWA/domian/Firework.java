package FWA.domian;

import java.util.Arrays;

import ABC.domain.FoodSource;
import FA.domain.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * �̻�
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Firework implements Comparable {
	/**
	 * λ��
	 */
	private Position position;
	/**
	 * �������
	 */
	private double light;

	private double dis;

	public Firework(Position position) {
		super();
		this.position = position;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// ��С����
		double tmp = this.light - ((Firework) (o)).getLight();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Firework [ " + Arrays.toString(position.getPositionCode()) + ", light=" + light + "]";
	}
}
