package BA.domain;

import java.util.Arrays;

import AFSA.domain.Fish;
import Common.Position;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Bat implements Comparable {
	/**
	 * ����λ��
	 */
	private Position position;
	/**
	 * �ٶ�
	 */
	private double[] velocity;
	/**
	 * ��Ӧ��
	 */
	private double fitness = Double.MAX_VALUE;

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// ��С����
		double tmp = this.fitness - ((Bat) (o)).getFitness();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Pisition [ " + Arrays.toString(position.getPositionCode()) + ", fitness=" + fitness + "]";
	}

	public Bat(Position position) {
		super();
		this.position = position;
		this.velocity = new double[position.getDimension()];
	}
}
