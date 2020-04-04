package HS.domain;

import java.util.Arrays;

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
public class Harmony implements Comparable {
	/**
	 * ͨ���������۸�����Ʒ�ʣ���Ŀ�꺯��
	 */
	private double performence;

	/**
	 * �����ĺϲ�
	 */
	private Composition composition;

	public Harmony(Composition composition) {
		super();
		this.composition = composition;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// ��С����
		double tmp = this.performence - ((Harmony) (o)).getPerformence();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Harmony [ " + Arrays.toString(composition.getPitches()) + ", performence=" + performence + "]";
	}
}
