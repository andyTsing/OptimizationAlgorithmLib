package SA.algorithm;

import java.util.Random;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import FA.domain.Position;
import SA.domain.Substance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ģ���˻�Simulated Annealing��SA
 * 
 * @author hba
 *
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class SANormal implements SAIface {
	/**
	 * ���Ž�
	 */
	protected Substance bestSubstance;
	/**
	 * ��ʼ�¶�lambda
	 */
	protected double T0;

	/**
	 * ��ƽ�⣨ÿ���¶��������ɵĽ��������
	 */
	protected double beta;

	/**
	 * �¶��½���
	 */
	@Builder.Default
	protected double alpha = 0.98;

	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ά��
	 */
	protected int dim;
	/**
	 * ����������
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * �����
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * Ŀ�꺯��
	 */
	protected ObjectiveFun objectiveFun;

	@Override
	public void start() {
		Substance substance = genInitSolution();
		bestSubstance = new Substance(new Position(dim, objectiveFun.getRange()));
		bestSubstance.getPosition().setPositionCode(substance.getPosition().getPositionCode());
		bestSubstance.setEnergy(substance.getEnergy());
		double T = T0;
		while (iterator < maxGen) {
			for (int j = 0; j < beta; j++) {
				Substance newSubstance = genNewSolution(substance);
				// ����������
				if (newSubstance.getEnergy() > bestSubstance.getEnergy()) {
					substance = newSubstance;
				} else {
					double deldaF = Math.abs(newSubstance.getEnergy() - bestSubstance.getEnergy());
					double p = Math.pow(Math.E, -deldaF / T);
					double rand = random.nextDouble();
					if (rand <= p) {
						substance = newSubstance;
					}
				}
			}
			if (substance.getEnergy() > bestSubstance.getEnergy()) {
				bestSubstance.getPosition().setPositionCode(substance.getPosition().getPositionCode());
				bestSubstance.setEnergy(substance.getEnergy());
			}
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestSubstance + "**********");
			T = T * alpha;
		}
	}

	public void incrementIter() {
		iterator++;
	}

	private Substance genNewSolution(Substance oldSubstance) {
		// TODO Auto-generated method stub
		Substance newSubstance = new Substance(new Position(dim, objectiveFun.getRange()));
		double[] newPositionCode = IntStream.range(0, this.dim)
				.mapToDouble(ind -> oldSubstance.getPosition().getPositionCode()[ind]
						+ 0.1 * objectiveFun.getRange().getScale()[ind] * (random.nextDouble() - 0.5))
				.toArray();
		newSubstance.getPosition().setPositionCode(newPositionCode);
		newSubstance.setEnergy(this.objectiveFun.getObjValue(newSubstance.getPosition().getPositionCode()));
		return newSubstance;

	}

	private Substance genInitSolution() {
		// TODO Auto-generated method stub
		Substance substance = new Substance(new Position(dim, objectiveFun.getRange()));
		substance.setEnergy(objectiveFun.getObjValue(substance.getPosition().getPositionCode()));
		return substance;
	}
}
