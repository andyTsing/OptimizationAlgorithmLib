package ABC.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import Common.ObjectiveFun;
import ES.domain.Code;
import ES.domain.Individual;
import FA.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ABCNormal implements ABCIface {
	/**
	 * ʳ��Դ
	 */
	protected List<FoodSource> foodSources;
	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ʳ��������������ֵ��Ϊʳ��Դ�ľ�
	 */
	protected int foodLimit;
	/**
	 * ά��
	 */
	protected int dim;
	/**
	 * ʳ��Դ����
	 */
	protected int foodSourcesAmout;
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
		foodSources = new ArrayList<>();
		for (int i = 0; i < foodSourcesAmout; i++) {
			foodSources.add(new FoodSource(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	/**
	 * ����ʳ��������������̣�
	 */
	public void calcFoodAmount() {
		for (FoodSource foodSource : foodSources) {
			foodSource.setFoodAmount(this.getObjectiveFun().getObjValue(foodSource.getPosition().getPositionCode()));
		}
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		FoodSource bestFoodSource = new FoodSource(new Position(this.dim, this.objectiveFun.getRange()));
		if (this.random == null) {
			this.random = new Random();
		}
		initPop();
		while (this.iterator < maxGen) {
			// ��Ӷ��׶�
			employeeBeePhase(foodSources);
			onlookerBeePhase(foodSources);
			bestFoodSource = keepBest(bestFoodSource, foodSources);
			scoutBeePhase(foodSources);
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestFoodSource + "**********");
		}
	}

	/**
	 * ��¼��ǰ���Ž�
	 * 
	 * @param bestFoodSource
	 * @param foodSources
	 */
	private FoodSource keepBest(FoodSource bestFoodSource, List<FoodSource> foodSources) {
		// TODO Auto-generated method stub
		FoodSource currBest = foodSources.get(foodSources.size() - 1);
		if (currBest.getFoodAmount() > bestFoodSource.getFoodAmount()) {
			bestFoodSource.setFoodAmount(currBest.getFoodAmount());
			bestFoodSource.getPosition().setPositionCode(currBest.getPosition().getPositionCode());
			bestFoodSource.setTrials(currBest.getTrials());
		}
		return bestFoodSource;
	}

	@Override
	public void employeeBeePhase(List<FoodSource> foodSources) {
		// TODO Auto-generated method stub
		for (FoodSource foodSourcei : foodSources) {
			// ���ѡ����һ��ʳ��Դj,��֤foodSourcei��foodSourcej
			int indj = random.nextInt(foodSources.size());
			while (indj == foodSources.indexOf(foodSourcei)) {
				indj = random.nextInt(foodSources.size());
			}
			final FoodSource foodSourcej = foodSources.get(indj);
			double phi = 2 * random.nextDouble() - 1;
			double[] newPosCode = IntStream.range(0, this.dim)
					.mapToDouble(i -> foodSourcei.getPosition().getPositionCode()[i]
							+ phi * (foodSourcei.getPosition().getPositionCode()[i]
									- foodSourcej.getPosition().getPositionCode()[i]))
					.toArray();
			double newFoodAmout = this.objectiveFun.getObjValue(newPosCode);
			// ̰��ѡ����ḻ��ʳ��Դ
			if (newFoodAmout > foodSourcei.getFoodAmount()) {
				foodSourcei.getPosition().setPositionCode(newPosCode);
				foodSourcei.setFoodAmount(newFoodAmout);
				foodSourcei.setTrials(0);
			} else {
				foodSourcei.setTrials(foodSourcei.getTrials() + 1);
			}
		}
	}

	@Override
	public void onlookerBeePhase(List<FoodSource> foodSources) {
		// TODO Auto-generated method stub
		// ���̶�ѡ��
		double minAmout = foodSources.stream().mapToDouble(f -> f.getFoodAmount()).min().getAsDouble();
		double totalAmount = foodSources.stream().mapToDouble(f -> f.getFoodAmount() - minAmout).sum();
		double rand = random.nextDouble();
		double accuProb = 0;
		int indi = 0;
		for (FoodSource foodSource : foodSources) {
			accuProb += (foodSource.getFoodAmount() - minAmout) / totalAmount;
			if (rand <= accuProb) {
				indi = foodSources.indexOf(foodSource);
				break;
			}
		}
		final FoodSource foodSourcei = foodSources.get(indi);
		// ���ѡ����һ��ʳ��Դj,��֤foodSourcei��foodSourcej
		int indj = random.nextInt(foodSources.size());
		while (indj == indi) {
			indj = random.nextInt(foodSources.size());
		}
		final FoodSource foodSourcej = foodSources.get(indj);
		double phi = 2 * random.nextDouble() - 1;
		double[] newPosCode = IntStream.range(0, this.dim)
				.mapToDouble(i -> foodSourcei.getPosition().getPositionCode()[i]
						+ phi * (foodSourcei.getPosition().getPositionCode()[i]
								- foodSourcej.getPosition().getPositionCode()[i]))
				.toArray();
		double newFoodAmout = this.objectiveFun.getObjValue(newPosCode);
		// ̰��ѡ����ḻ��ʳ��Դ
		if (newFoodAmout > foodSourcei.getFoodAmount()) {
			foodSourcei.getPosition().setPositionCode(newPosCode);
			foodSourcei.setFoodAmount(newFoodAmout);
			foodSourcei.setTrials(0);
		} else {
			foodSourcei.setTrials(foodSourcei.getTrials() + 1);
		}

	}

	@Override
	public void scoutBeePhase(List<FoodSource> foodSources) {
		// TODO Auto-generated method stub
		for (FoodSource foodSource : foodSources) {
			if (foodSource.getTrials() > this.foodLimit) {
				double[] positionCode = new double[this.dim];
				for (int i = 0; i < this.dim; i++) {
					positionCode[i] = foodSource.getPosition().getRange().getLow()[i]
							+ (foodSource.getPosition().getRange().getHigh()[i]
									- foodSource.getPosition().getRange().getLow()[i]) * Math.random();
				}
				foodSource.getPosition().setPositionCode(positionCode);
				foodSource.setFoodAmount(this.objectiveFun.getObjValue(positionCode));
				foodSource.setTrials(0);
			}
		}
	}
}
