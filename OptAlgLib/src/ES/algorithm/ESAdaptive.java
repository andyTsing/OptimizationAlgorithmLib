package ES.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import ES.domain.Code;
import ES.domain.Individual;
import ES.operation.Recombination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class ESAdaptive implements ESIface {
	/**
	 * ��Ⱥ
	 */
	protected List<Individual> population;
	/**
	 * ����ǿ��
	 */
	@Builder.Default
	protected double mutStrength = 5;
	/**
	 * ������
	 */
	protected int maxGen;
	/**
	 * ��Ⱥ��С��
	 */
	protected int popNum;
	/**
	 * �Ӵ�������
	 */
	protected int childNum;
	/**
	 * ά��
	 */
	protected int dim;
	/**
	 * Ŀ�꺯��
	 */
	protected ObjectiveFun objectiveFun;
	/**
	 * ����ӦES
	 */
	@Builder.Default
	protected boolean isAdaptive = false;
	/**
	 * �Ƿ�ϲ��������Ӵ�
	 */
	@Builder.Default
	protected boolean concatParentandChild = false;
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
	 * �������Ӧ�
	 */
	private int crossNum;
	/**
	 * ����
	 */
	private Recombination recombination;

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		if (this.random == null) {
			this.random = new Random();
		}
		initPop();
		while (this.iterator < maxGen) {
			List<Individual> newPopulation = this.makeNewIndividuals(population);
			Individual bestIndividual = killBadIndividuals(newPopulation);
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestIndividual + "**********");
		}
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		population = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			population.add(new Individual(new Code(this.dim, objectiveFun.getRange(), this.mutStrength)));
		}

	}

	@Override
	public List<Individual> makeNewIndividuals(List<Individual> population) {
		// TODO Auto-generated method stub
		List<Individual> children = new ArrayList<>();
		try {
			for (int i = 0; i < this.childNum; i++) {
				List<Individual> selectedParentsIndividuals = random.ints(crossNum, 0, population.size())
						.mapToObj(ind -> population.get(ind)).collect(Collectors.toList());

				Individual newIndividual = this.recombination.crossover(selectedParentsIndividuals);

				double t0 = Math.pow(2 * this.dim, -0.5);

				double t = Math.pow(2 * Math.pow(this.dim, 0.5), -0.5);

				double random0 = random.nextGaussian();

				double[] newMutStrength = IntStream.range(0, this.dim)
						.mapToDouble(ind -> newIndividual.getCode().getMutStrength()[ind]
								* Math.pow(Math.E, t0 * random0 + t * random.nextGaussian()))
						.toArray();

				double[] newCode = IntStream.range(0, this.dim).mapToDouble(
						ind -> newIndividual.getCode().getCode()[ind] + Math.sqrt(mutStrength) * random.nextGaussian())
						.toArray();
				newIndividual.getCode().setCode(newCode);
				newIndividual.getCode().setMutStrength(newMutStrength);
				children.add(newIndividual);
			}
			// �ж��Ƿ�ϲ��������Ӵ��������Ϊ(1+1)-ES����ϲ�
			if (concatParentandChild || (this.childNum == 1 && this.popNum == 1)) {
				children.addAll(population);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return children;
	}

	@Override
	public Individual killBadIndividuals(List<Individual> population) throws Exception {
		Individual bestIndividual = null;
		// ����
		for (Individual individual : population) {
			individual.setFitness(this.getObjectiveFun().getObjValue((individual.getCode().getCode())));
		}
		// ���Ƚ�������
		Collections.sort(population);
		// ��(��,��)-ES�б��뱣֤��<��
		if (popNum > population.size()) {
			throw new Exception("���������ø�����Ⱥ��С(��)���Ӵ���Ⱥ��С(��)���Ա�֤��<��,һ��Ҫ��5��=�� !!!");
		}
		// �����������⣬��ȡǰ�˸����壨��Ϊ�����ǰ�����Ӧ��ֵ�Ӵ�С���еģ�
		if (this.getObjectiveFun().getDirection() == ObjectiveFun.Max) {
			this.population = population.subList(0, popNum);
			bestIndividual = population.get(0);
		} else {
			this.population = population.subList(population.size() - 1 - popNum, population.size() - 1);
			bestIndividual = population.get(population.size() - 1);
		}
		return bestIndividual;
	}

}
