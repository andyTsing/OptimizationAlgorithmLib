package SSA.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import AFSA.domain.Fish;
import Common.ObjectiveFun;
import Common.Position;
import SSA.domain.Spider;
import Util.MathUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ���֩���㷨(Social Spider Algorithm ��SSA)
 * 
 * @author hba
 *
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class SSANormal implements SSAIface {
	/**
	 * ���֩��Ⱥ
	 */
	protected List<Spider> spiders;
	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ά��
	 */
	protected int dim;
	/**
	 * ��˥����
	 */
	protected double ra;
	/**
	 * ����mask���ļ���
	 */
	protected double pc;
	/**
	 * ����Ϊ1�ĸ���
	 */
	protected double pm;
	/**
	 * ֩�����
	 */
	protected int spiderAmount;
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
	 * ����֩��
	 */
	@Builder.Default
	protected Spider bestSpider;
	/**
	 * Ŀ�꺯��
	 */
	protected ObjectiveFun objectiveFun;

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		spiders = new ArrayList<>();
		for (int i = 0; i < spiderAmount; i++) {
			spiders.add(new Spider(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	private void calcFitnessAndVibration() {
		// TODO Auto-generated method stub
		for (Spider spider : spiders) {
			spider.setFitness(this.objectiveFun.getObjValue(spider.getPosition().getPositionCode()));
			double I = Math.log(1 + 1.0 / (spider.getFitness() + Math.pow(10, -100)/* C */));
			spider.setVibration(I);
		}
		Collections.sort(spiders);
		if (spiders.get(0).getFitness() < bestSpider.getFitness()) {
			bestSpider.setFitness(spiders.get(0).getFitness());
			bestSpider.getPosition().setPositionCode(spiders.get(0).getPosition().getPositionCode());
		}
	}

	@Override
	public void start() {
		bestSpider = new Spider(new Position(this.dim, this.objectiveFun.getRange()));
		bestSpider.setFitness(Double.MAX_VALUE);
		initPop();
		while (this.iterator < maxGen) {
			// ������Ӧ��ֵ����
			calcFitnessAndVibration();
			// ����ά�ȱ�׼���ƽ��ֵ
			double[] averageArray = IntStream.range(0, this.dim).mapToDouble(dim -> {
				return spiders.stream().mapToDouble(spider -> spider.getPosition().getPositionCode()[dim]).average()
						.getAsDouble();
			}).toArray();

			double meanDeviation = IntStream.range(0, this.dim).mapToDouble(dim -> {
				return Math.pow(spiders.stream()
						.mapToDouble(
								spider -> Math.pow(spider.getPosition().getPositionCode()[dim] - averageArray[dim], 2))
						.sum() / this.spiderAmount, 0.5);
			}).average().getAsDouble();

			for (Spider spidera : spiders) {
				// ���㴫�������V
				List<Double> V = new ArrayList<Double>();
				for (Spider spiderb : spiders) {
					double distance = 0;
					if (spidera != spiderb) {
						distance = MathUtil.calcManhattanDistance(spidera.getPosition().getPositionCode(),
								spiderb.getPosition().getPositionCode());
					}
					double newIntensity = spiderb.getVibration() * Math.exp(-distance / (meanDeviation * ra));
					V.add(newIntensity);
				}
				// ��V��ѡ��������
				double vbest = V.stream().mapToDouble(v -> v).max().getAsDouble();
				int bestInd = V.indexOf(vbest);
				// ��������Ŀ����
				if (vbest > spidera.getTargetVibration()) {
					spidera.setTargetVibration(vbest);
					spidera.setTargetPosition(spiders.get(bestInd).getPosition().getPositionCode());
					// ����Cs
					spidera.setCs(0);
				} else {
					// ����Cs
					spidera.setCs(spidera.getCs() + 1);
				}
				double r = random.nextDouble();
				// �ܶ��csû�и���ʱ�����и�����ܸ�������
				if (r > Math.pow(this.pc, spidera.getCs())) {
					// ����������룬����pm�ĸ��ʱ�Ϊ1��
					// ����ȫ��Ϊ1��Ϊ0ʱ�����ѡ��ĳһά�Ƚ��и���
//					long zeroLen = Arrays.stream(spidera.getMask()).filter(i -> i == 0).count();
//					long oneLen = Arrays.stream(spidera.getMask()).filter(i -> i == 1).count();
//					// ȫΪ1
//					if (zeroLen == 0) {
//						spidera.getMask()[random.nextInt(this.dim)] = 0;
//					}
//					// ȫΪ0
//					if (oneLen == 0) {
//						spidera.getMask()[random.nextInt(this.dim)] = 1;
//					}

					for (int i = 0; i < this.dim; i++) {
						double rr = random.nextDouble();
						// �Ը���pm����Ϊ1
						if (rr <= this.pm) {
							spidera.getMask()[i] = 1;
						} else {
							spidera.getMask()[i] = 0;
						}
					}
				}
				// ����pfollow
				double[] pfollow = new double[this.dim];
				for (int maskInd = 0; maskInd < spidera.getMask().length; maskInd++) {
					// ��Ŀ��λ����ѡ��
					if (spidera.getMask()[maskInd] == 0) {
						pfollow[maskInd] = spidera.getTargetPosition()[maskInd];
					} else {
						// �����ѡ���֩����ѡ��
						pfollow[maskInd] = spiders.get(random.nextInt(this.spiderAmount)).getPosition()
								.getPositionCode()[maskInd];
					}
				}
				// ������ߣ�������һ�ε�����λ��
				double[] currPos = spidera.getPosition().getPositionCode();
				double[] movement = IntStream.range(0, this.dim)
						.mapToDouble(dim -> spidera.getMovenment()[dim] * random.nextDouble()
								+ (pfollow[dim] - currPos[dim]) * random.nextDouble())
						.toArray();
				double[] newPos = IntStream.range(0, this.dim).mapToDouble(dim -> currPos[dim] + movement[dim])
						.toArray();
				spidera.getPosition().setPositionCode(newPos);
				spidera.setMovenment(movement);
			}
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestSpider + "**********");
			incrementIter();
		}
	}
}
