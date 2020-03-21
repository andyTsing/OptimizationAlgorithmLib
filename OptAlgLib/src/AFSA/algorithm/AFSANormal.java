/**
 * 
 */
package AFSA.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import AFSA.domain.Fish;
import Common.ObjectiveFun;
import FA.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hba �˹���Ⱥ�㷨
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class AFSANormal implements AFSA {
	/**
	 * ʳ��Դ
	 */
	protected List<Fish> fishes;
	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ����
	 */
	protected int fishAmount;
	/**
	 * ���Դ���
	 */
	protected int trials = 0;
	/**
	 * ӵ������
	 */
	protected double delta;
	/**
	 * �Ӿ�
	 */
	protected double visual;
	/**
	 * ����
	 */
	protected double step;
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
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		fishes = new ArrayList<>();
		for (int i = 0; i < fishAmount; i++) {
			fishes.add(new Fish(new Position(this.dim, objectiveFun.getRange())));
		}
		calcFoodAmount();
	}

	private void calcFoodAmount() {
		// TODO Auto-generated method stub
		for (Fish fish : fishes) {
			fish.setFoodAmount(this.objectiveFun.getObjValue(fish.getPosition().getPositionCode()));
		}
		Collections.sort(fishes);
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		Fish bestFish = new Fish(new Position(this.dim, this.objectiveFun.getRange()));
		if (this.random == null) {
			this.random = new Random();
		}
		initPop();
		bestFish = fishes.get(fishes.size() - 1);
		while (this.iterator < maxGen) {
			for (Fish fish : fishes) {
				// Ⱥ��
				Fish swarmFish = AF_Swarm(fish);
				// ׷��
				Fish followFish = AF_Follow(fish);
				// ��ʳ
				Fish preyFish = AF_Prey(fish);
				// ����
				if (swarmFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = swarmFish;
				}
				if (followFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = followFish;
				}
				if (preyFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = preyFish;
				}
			}
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestFish + "**********");
		}
	}

	@Override
	public Fish AF_Prey(Fish fish) {
		// TODO Auto-generated method stub
		Fish preyFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		// ���Դ���
		for (int t = 0; t < this.trials; t++) {
			// ���Ȱ����Ӿ��������һ��λ��
			double[] newPositionCode = IntStream.range(0, this.dim)
					.mapToDouble(i -> fish.getPosition().getPositionCode()[i] + random.nextDouble() * this.visual)
					.toArray();
			preyFish.getPosition().setPositionCode(newPositionCode);
			double newFoodAmount = this.objectiveFun.getObjValue(preyFish.getPosition().getPositionCode());
			preyFish.setFoodAmount(newFoodAmount);
			// ����������λ���и����ʳ������λ���ƶ�
			if (newFoodAmount > fish.getFoodAmount()) {
				return preyFish;
			}
		}
		// ����һ�������󣬰��ղ������ѡ��λ��
		double[] newPositionCode = IntStream.range(0, this.dim)
				.mapToDouble(i -> fish.getPosition().getPositionCode()[i] + random.nextDouble() * this.step).toArray();
		preyFish.getPosition().setPositionCode(newPositionCode);
		double newFoodAmount = this.objectiveFun.getObjValue(preyFish.getPosition().getPositionCode());
		preyFish.setFoodAmount(newFoodAmount);
		return preyFish;
	}

	@Override
	public Fish AF_Follow(Fish fish) {
		// TODO Auto-generated method stub
		Fish followFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		double maxFoodAmount = Double.NEGATIVE_INFINITY;
		// ���λ��
		Position maxpc = new Position(dim, null);
		// �����
		Fish maxFish;
		Fish maxFish_ = null;
		double nf;
		double nf_ = 0;
		for (Fish fishj : fishes) {
			if (fishj == fish) {
				continue;
			}
			if (calcDistance(fish, fishj) < visual && fishj.getFoodAmount() > maxFoodAmount) {
				maxFish_ = fishj;
				maxFoodAmount = fishj.getFoodAmount();
				maxpc.setPositionCode(fishj.getPosition().getPositionCode());
			}
		}
		// ��ǰfish�Ӿ�������fish������
		for (Fish fishj : fishes) {
			if (fishj == maxFish_) {
				continue;
			}
			if (calcDistance(maxFish_, fishj) < visual) {
				nf_++;
			}
		}
		nf = nf_;

		// �Ӿ���û����
		if (maxFish_ == null) {
			maxFish = fish;
		} else {
			maxFish = maxFish_;
		}
		// ��̫ӵ�������Ŵ�ʳ�����
		if (nf / this.fishAmount < this.delta && maxFish.getFoodAmount() > fish.getFoodAmount()) {
			double[] newPositionCode = IntStream.range(0, this.dim)
					.mapToDouble(i -> fish.getPosition().getPositionCode()[i]
							+ (maxFish.getPosition().getPositionCode()[i] - fish.getPosition().getPositionCode()[i])
									/ calcDistance(maxFish, fish) * step * random.nextDouble())
					.toArray();
			followFish.getPosition().setPositionCode(newPositionCode);
			// ע��һ������������ȡλ�ã���Ϊ�����λ�ø�ֵ�����߽�
			followFish.setFoodAmount(this.objectiveFun.getObjValue(followFish.getPosition().getPositionCode()));

		} else {
			return AF_Prey(fish);
		}
		return followFish;
	}

	@Override
	public Fish AF_Swarm(Fish fish) {
		// TODO Auto-generated method stub
		Fish swarmFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		// �Ӿ����������
		double nf;
		double nf_ = 0;
		// ����λ��
		Position xc = new Position(dim, null);
		for (Fish fishj : fishes) {
			if (fishj == fish) {
				continue;
			}
			if (calcDistance(fish, fishj) < visual) {
				nf_++;
				double[] xcSum = IntStream.range(0, this.dim)
						.mapToDouble(i -> xc.getPositionCode()[i] + fishj.getPosition().getPositionCode()[i]).toArray();
				xc.setPositionCode(xcSum);
			}
		}
		nf = nf_;
		double[] xcCode = nf_ == 0 ? fish.getPosition().getPositionCode()
				: IntStream.range(0, this.dim).mapToDouble(i -> xc.getPositionCode()[i] / nf).toArray();
		double cxFoodAmount = this.getObjectiveFun().getObjValue(xcCode);
		// ��̫ӵ��������ʳ�����
		if (nf / this.fishAmount < this.delta && cxFoodAmount > fish.getFoodAmount()) {
			double[] newPositionCode = IntStream.range(0, this.dim).mapToDouble(
					i -> fish.getPosition().getPositionCode()[i] + (xcCode[i] - fish.getPosition().getPositionCode()[i])
							/ calcDistance(xcCode, fish.getPosition().getPositionCode()) * step * random.nextDouble())
					.toArray();
			swarmFish.getPosition().setPositionCode(newPositionCode);
			// ע��һ������������ȡλ�ã���Ϊ�����λ�ø�ֵ�����߽�
			swarmFish.setFoodAmount(this.objectiveFun.getObjValue(swarmFish.getPosition().getPositionCode()));

		} else {
			return AF_Prey(fish);
		}

		return swarmFish;
	}

	private double calcDistance(Fish fishi, Fish fishj) {
		// TODO Auto-generated method stub
		if (fishi == null || fishj == null) {
			return Double.POSITIVE_INFINITY;
		}
		return calcDistance(fishi.getPosition().getPositionCode(), fishj.getPosition().getPositionCode());
	}

	private double calcDistance(double[] positionCodei, double[] positionCodej) {
		// TODO Auto-generated method stub
		double sumDis = IntStream.range(0, positionCodei.length)
				.mapToDouble(i -> Math.pow(positionCodei[i] - positionCodej[i], 2)).sum();
		return Math.sqrt(sumDis);
	}

}
