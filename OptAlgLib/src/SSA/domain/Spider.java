package SSA.domain;

import java.util.Arrays;

import Common.Position;
import FA.domain.Firefly;
import lombok.Getter;
import lombok.Setter;

/**
 * ֩��
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Spider implements Comparable {
	/**
	 * ֩����֩�����ϵ�λ��
	 */
	private Position position;
	/**
	 * ��Ӧ��ֵ
	 */
	private double fitness;
	/**
	 * Ŀ����
	 */
	private double targeVibration = 0;
	/**
	 * Ŀ��λ��
	 */
	private double[] targePosition;
	/**
	 * ��
	 */
	private double vibration;
	/**
	 * ��һ��Ŀ���񶯺�ĵ�������
	 */
	private int cs;
	/**
	 * ��һ�ε�λ��
	 */
	private double[] lastPosition;
	/**
	 * �����ƶ���ά������
	 */
	private int[] mask;

	public Spider(Position position) {
		super();
		this.position = position;
		this.mask = new int[position.getDimension()];
		this.lastPosition = new double[position.getDimension()];
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		double tmp = this.fitness - ((Spider) (o)).getFitness();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Spider [ " + Arrays.toString(position.getPositionCode()) + ", fitness=" + fitness + "]";
	}
}
