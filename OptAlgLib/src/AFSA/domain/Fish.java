package AFSA.domain;

import java.util.Arrays;

import Common.Position;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Fish implements Comparable {
	/**
	 * ʳ��Դλ��
	 */
	private Position position;
	/**
	 * ʳ������
	 */
	private double foodAmount = -Double.MAX_VALUE;
	/**
	 * �Ӿ�
	 */
	private double visual;

	public Fish(Position position) {
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
		double tmp = this.foodAmount - ((Fish) (o)).getFoodAmount();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Pisition [ " + Arrays.toString(position.getPositionCode()) + ", foodAmount=" + foodAmount + "]";
	}

}
