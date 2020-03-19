package ES.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import ES.domain.Code;
import ES.domain.Individual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ESNormal implements ESIface {
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
	 * �ɹ��������
	 */
	@Builder.Default
	protected int successMut = 0;
	/**
	 * ͳ�Ʒ�ΧG
	 */
	@Builder.Default
	protected int G = 20;
	/**
	 * �����
	 */
	@Builder.Default
	protected Random random = new Random();

	public ESNormal() {
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********��Ⱥ��ʼ��**********");
		population = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			population.add(new Individual(new Code(this.dim, objectiveFun.getRange())));
		}
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		initPop();
		while (this.iterator < maxGen) {
			List<Individual> newPopulation = this.makeNewIndividuals(population);
			Individual bestIndividual = killBadIndividuals(newPopulation);
			incrementIter();
			System.out.println("**********��" + iterator + "�����Ž⣺" + bestIndividual + "**********");
		}
	}

	@Override
	public List<Individual> makeNewIndividuals(List<Individual> population) {
		// TODO Auto-generated method stub
		List<Individual> children = new ArrayList<>();
		try {
			for (int i = 0; i < this.childNum; i++) {
				int parentInd = random.nextInt(population.size());
				Individual parent = population.get(parentInd);
				Individual child = parent.clone();
				double[] newCode = IntStream.range(0, this.dim)
						.mapToDouble(
								ind -> child.getCode().getCode()[ind] + Math.sqrt(mutStrength) * random.nextGaussian())
						.toArray();
				child.getCode().setCode(newCode);
				children.add(child);
			}
			// �ж��Ƿ�ϲ��������Ӵ��������Ϊ(1+1)-ES����ϲ�
			if (concatParentandChild || (this.childNum == 1 && this.popNum == 1)) {
				children.addAll(population);
			}
		} catch (CloneNotSupportedException e) {
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
		// ���Ϊ(1+1)-ES,��Ҫ��������Ӧ��������ǿ��
		if (popNum == 1 && childNum == 1) {
			// ���Ŀ��Ļ����ж��ǲ��ǳɹ����죬Ҫ�ж��Ӵ���Ӧ���Ƿ���ڸ�����Ӧ��
			if (this.getObjectiveFun().getDirection() == ObjectiveFun.Max) {
				if (population.get(0).getFitness() > population.get(1).getFitness()) {
					successMut++;
				}
			} else {
				if (population.get(0).getFitness() < population.get(1).getFitness()) {
					successMut++;
				}
			}
		}
		if ((this.iterator + 1) % G == 0) {
			// ����0.2�����ӱ���ǿ��
			if (successMut / G > 0.2) {
				this.mutStrength = this.mutStrength / 0.817;
			} else if (successMut / G < 0.2) {
				this.mutStrength = this.mutStrength * 0.817;
			}
			successMut = 0;
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
