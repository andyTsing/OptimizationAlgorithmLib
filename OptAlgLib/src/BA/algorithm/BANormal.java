package BA.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import AFSA.algorithm.AFSANormal;
import AFSA.domain.Fish;
import BA.domain.Bat;
import Common.ObjectiveFun;
import Common.Position;
import io.jenetics.util.ISeq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ========================================================
 * 
 * Files of the Matlab programs included in the book: Xin-She Yang,
 * Nature-Inspired Metaheuristic Algorithms, Second Edition, Luniver Press,
 * (2010). www.luniver.com
 * 
 * ========================================================
 * 
 * --------------------------------------------------------
 * 
 * Bat-inspired algorithm for continuous optimization (demo) Programmed by
 * Xin-She Yang @Cambridge University 2010
 * 
 * --------------------------------------------------------
 * 
 * Usage: bat_algorithm([20 1000 0.5 0.5]);
 * 
 * -------------------------------------------------------------------
 * 
 * This is a simple demo version only implemented the basic idea of the bat
 * algorithm without fine-tuning the parameters, Then, though this demo works
 * very well, it is expected that this demo is much less efficient than the work
 * reported in the following papers:
 * 
 * (Citation details):
 * 
 * 1) Yang X.-S., A new metaheuristic bat-inspired algorithm, in: Nature
 * Inspired Cooperative Strategies for Optimization (NISCO 2010) (Eds. J. R.
 * Gonzalez et al.), Studies in Computational Intelligence, Springer, vol. 284,
 * 65-74 (2010).
 * 
 * 2) Yang X.-S., Nature-Inspired Metaheuristic Algorithms, Second Edition,
 * Luniver Presss, Frome, UK. (2010).
 * 
 * 3) Yang X.-S. and Gandomi A. H., Bat algorithm: A novel approach for global
 * engineering optimization, Engineering Computations, Vol. 29, No. 5, pp.
 * 464-483 (2012).
 * 
 * -------------------------------------------------------------------
 * 
 * @author hba
 *
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class BANormal implements BAIFace {
	/**
	 * ������Ⱥ
	 */
	protected List<Bat> bats;
	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ��������
	 */
	protected int batAmount;
	/**
	 * ���
	 */
	protected double loudness;
	/**
	 * ���˥��������0��С��1��
	 */
	@Builder.Default
	protected double loudnessAttenuation = 0;
	/**
	 * ����Ƶ��
	 */
	protected double pulseRate;
	/**
	 * ����Ƶ��˥��������1��
	 */
	@Builder.Default
	protected double pulseRateAttenuation = 0;
	/**
	 * ����������
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * Ƶ����Сֵ
	 */
	protected double frequencyMin;
	/**
	 * Ƶ�����ֵ
	 */
	protected double frequencyMax;
	/**
	 * ά��
	 */
	protected int dim;
	/**
	 * �����
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * Ŀ�꺯��
	 */
	protected ObjectiveFun objectiveFun;
	/**
	 * ��������
	 */
	protected Bat bestBat;

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		bats = new ArrayList<>();
		for (int i = 0; i < batAmount; i++) {
			bats.add(new Bat(new Position(this.dim, objectiveFun.getRange())));
		}
		calcFitness();
	}

	private void calcFitness() {
		// TODO Auto-generated method stub
		if (bestBat == null) {
			bestBat = new Bat(new Position(this.dim, objectiveFun.getRange()));
		}
		for (Bat bat : bats) {
			bat.setFitness(this.objectiveFun.getObjValue(bat.getPosition().getPositionCode()));
		}
		Collections.sort(bats);
		Bat currBestBat = bats.get(0);
		if (objectiveFun.getDirection() == ObjectiveFun.Max) {
			currBestBat = bats.get(bats.size() - 1);
		}
		bestBat.setFitness(currBestBat.getFitness());
		bestBat.getPosition().setPositionCode(currBestBat.getPosition().getPositionCode());
	}

	public void incrementIter() {
		iterator++;
		if (loudnessAttenuation != 0) {
			loudness = loudness * loudnessAttenuation;
		}
		if (pulseRateAttenuation != 0) {
			pulseRate = pulseRate * (1 - Math.exp(-pulseRateAttenuation * iterator));
		}
	}

	@Override
	public void start() throws Exception {
		initPop();
		while (this.iterator < maxGen) {
			for (Bat bat : bats) {
				double frequency = this.frequencyMin + (this.frequencyMax - this.frequencyMin) * random.nextDouble();
				double[] vel = IntStream.range(0, this.dim)
						.mapToDouble(d -> bat.getVelocity()[d]
								+ (bat.getPosition().getPositionCode()[d] - bestBat.getPosition().getPositionCode()[d])
										* frequency)
						.toArray();
				double[] newPos = IntStream.range(0, this.dim)
						.mapToDouble(d -> bat.getPosition().getPositionCode()[d] + vel[d]).toArray();

				if (random.nextDouble() > pulseRate) {
					newPos = IntStream.range(0, this.dim)
							.mapToDouble(
									d -> bestBat.getPosition().getPositionCode()[d] + random.nextGaussian() * 0.001)
							.toArray();
				}

				// ��λ�õı߽紦��
				newPos = this.objectiveFun.getRange().bounds(newPos);

				double newFitness = this.objectiveFun.getObjValue(newPos);
				// ���µ�ǰ����
				if (this.objectiveFun.getDirection() * newFitness <= this.objectiveFun.getDirection() * bat.getFitness()
						&& random.nextDouble() < loudness) {
					bat.getPosition().setPositionCode(newPos);
					bat.setFitness(newFitness);
				}
				// ����ȫ����������
				if (this.objectiveFun.getDirection() * newFitness <= this.objectiveFun.getDirection()
						* bestBat.getFitness()) {
					bestBat.setFitness(newFitness);
					bestBat.getPosition().setPositionCode(newPos);
				}
			}
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestBat + "**********");
			incrementIter();
		}
	}
}
