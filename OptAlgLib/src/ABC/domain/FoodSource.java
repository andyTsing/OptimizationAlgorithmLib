package ABC.domain;

import java.util.Arrays;

import FA.domain.Firefly;
import FA.domain.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * sʳ��Դ
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class FoodSource implements Comparable {
	/**
	 * ʳ��Դλ��
	 */
	private Position position;
	/**
	 * ʳ������
	 */
	private double foodAmount = -Double.MAX_VALUE;
	/**
	 * ���Դ���
	 */
	private int trials = 0;

	public FoodSource(Position position) {
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
		double tmp = this.foodAmount - ((FoodSource) (o)).getFoodAmount();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "FoodSource [ " + Arrays.toString(position.getPositionCode()) + ", foodAmount=" + foodAmount + "]";
	}
}
