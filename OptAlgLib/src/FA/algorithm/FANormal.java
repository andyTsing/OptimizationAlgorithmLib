package FA.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import Common.Position;
import FA.domain.Firefly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ��׼FA�㷨
 * 
 * @author hba
 *
 */
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FANormal implements FireflyAlgorithm {
	/**
	 * ө������Ⱥ��ʾ
	 */
	protected List<Firefly> fireflies;
	/**
	 * ������
	 */
	private int maxGen;
	/**
	 * ��Ⱥ��С
	 */
	private int popNum;
	/**
	 * ά��
	 */
	private int dim;
	/**
	 * ������beta
	 */
	@Builder.Default
	private double initAttraction = 1.0;
	/**
	 * ���ʶԹ������ϵ��
	 */
	@Builder.Default
	private double gamma = 1.0;
	/**
	 * �Ŷ��Ĳ�������
	 */
	@Builder.Default
	private double alpha = 0.2;
	/**
	 * Ŀ�꺯��
	 */
	private ObjectiveFun objectiveFun;
	/**
	 * ����Ӧ����alpha
	 */
	@Builder.Default
	private boolean isAdaptive = false;
	/**
	 * ����alpha˥������delta
	 */
	@Builder.Default
	private double delta = 0.97;
	/**
	 * ����������
	 */
	@Builder.Default
	private int iterator = 0;
	/**
	 * �����
	 */
	@Builder.Default
	private Random random = new Random();
	/**
	 * �Ƿ�ʹ��matlab��ͼ
	 */
	@Builder.Default
	private boolean isDraw = true;

	/**
	 * 
	 * @param maxGen ����������
	 * @param popNum ��Ⱥ����
	 * @param dim    ά��
	 */
	public FANormal(int maxGen, int popNum, int dim) {

	}

	public void incrementIter() {
		iterator++;
		if (isAdaptive) {
			alpha *= delta;
		}
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		fireflies = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			fireflies.add(new Firefly(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	@Override
	public double calcDistance(double[] a, double[] b) {
		// TODO Auto-generated method stub
		assert a.length == b.length;
		double distance = 0;
		int n = a.length;
		distance = IntStream.range(0, n).mapToDouble(i -> (a[i] - b[i]) * (a[i] - b[i])).sum();
		return Math.sqrt(distance);
	}

	@Override
	public void fireflyMove() {
		// TODO Auto-generated method stub
		for (int i = 0; i < popNum; i++) {
			for (int j = 0; j < popNum; j++) {
				Firefly fireflyi = fireflies.get(i);
				Firefly fireflyj = fireflies.get(j);
				if (i != j && fireflyj.getLight() > fireflyi.getLight()) { // ��ө���j�����ȴ���ө���i������ʱ
					double[] codei = fireflyi.getPosition().getPositionCode();
					double[] codej = fireflyj.getPosition().getPositionCode();
					// ����ө���֮��ľ���
					double disij = calcDistance(codei, codej);
					// ����������beta
					double attraction = initAttraction * Math.pow(Math.E, -gamma * disij * disij); // ����ө���j��ө���i��������
					double[] scale = fireflyi.getPosition().getRange().getScale();
					double[] newPositionCode = IntStream.range(0, this.dim).mapToDouble(ind -> codei[ind]
							+ attraction * (codej[ind] - codei[ind]) + alpha * (random.nextDouble() - 0.5) * scale[ind])
							.toArray();
					fireflyi.getPosition().setPositionCode(newPositionCode);
					// ��������
					// fireflyi.setLight(this.getObjectiveFun().getObjValue(newPositionCode));
				}
			}
		}
		// ��������ө����������ƶ�
		Firefly bestFirefly = fireflies.get(popNum - 1);
		double[] scale = bestFirefly.getPosition().getRange().getScale();
		double[] newPositionCode = IntStream.range(0, dim).mapToDouble(
				i -> bestFirefly.getPosition().getPositionCode()[i] + alpha * (random.nextDouble() - 0.5) * scale[i])
				.toArray();
		bestFirefly.getPosition().setPositionCode(newPositionCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void calcuLight() {
		// TODO Auto-generated method stub
		for (Firefly firefly : fireflies) {
			firefly.setLight(this.getObjectiveFun().getObjValue((firefly.getPosition().getPositionCode())));
		}
		Collections.sort(fireflies);
		// չʾө���ֲ�
	}

	public void printFirflies() {
		for (Firefly firefly : fireflies) {
			System.out.println(firefly);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		initPop();
		while (this.iterator < maxGen) {
			calcuLight();
			fireflyMove();
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + fireflies.get(popNum - 1) + "**********");
		}
		System.out.println("------------------------------------");
		// printFirflies();
	}
}
