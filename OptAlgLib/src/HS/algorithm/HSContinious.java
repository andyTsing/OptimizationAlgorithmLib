package HS.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import Common.ObjectiveFun;
import Common.Position;
import Common.Range;
import HS.domain.Composition;
import HS.domain.Harmony;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class HSContinious implements HSIface {
	/**
	 * ���������
	 */
	protected List<Harmony> harmonyMemory;
	/**
	 * ����������С
	 */
	protected int HMS;
	/**
	 * ����������
	 */
	@Builder.Default
	protected double HMCR = 0.95;
	/**
	 * ����������
	 */
	@Builder.Default
	protected double PAR = 0.1;
	/**
	 * �������
	 */
	@Builder.Default
	protected double FW = 0.01;

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
	/**
	 * �������
	 */
	protected int maxI;

	/**
	 * ������
	 */
	protected int numOfInstruments;

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (this.random == null) {
			this.random = new Random();
		}
		initHM();
		while (this.iterator < maxI) {
			Harmony improvisedHarmony = improviseHarmony();
			updateHM(improvisedHarmony);
			System.out.println(
					"**********��" + iterator + "�����Ž⣺" + harmonyMemory.get(harmonyMemory.size() - 1) + "**********");
			incrementIter();
		}
	}

	/**
	 * ��ʼ�����������
	 */
	@Override
	public void initHM() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		System.out.println("**********����������ʼ��**********");
		harmonyMemory = new ArrayList<>();
		for (int i = 0; i < HMS; i++) {
			harmonyMemory.add(new Harmony(new Composition(this.numOfInstruments, objectiveFun.getRange())));
		}
		for (Harmony harmony : harmonyMemory) {
			harmony.setPerformence(this.objectiveFun.getObjValue(harmony.getComposition().getPitches()));
		}
		Collections.sort(harmonyMemory);
	}

	/**
	 * ��������
	 */
	@Override
	public Harmony improviseHarmony() {
		// TODO Auto-generated method stub
		Range range = objectiveFun.getRange();
		Harmony newHarmony = new Harmony(new Composition(this.numOfInstruments, range));
		double[] newPitches = newHarmony.getComposition().getPitches();
		for (int d = 0; d < numOfInstruments; d++) {
			double rnd = random.nextDouble();
			// �Ӻ����������ȡֵ
			if (rnd <= HMCR) {
				newPitches[d] = harmonyMemory.get(random.nextInt(HMS)).getComposition().getPitches()[d];
				// ��������
				double rnd2 = random.nextDouble();
				if (rnd2 <= PAR) {
					newPitches[d] = newPitches[d]
							+ (2 * random.nextDouble() - 1) * FW * (range.getHigh()[d] - range.getLow()[d]);
				}
			}
		}
		newHarmony.getComposition().setPitches(newPitches);
		newHarmony.setPerformence(this.objectiveFun.getObjValue(newHarmony.getComposition().getPitches()));
		return newHarmony;
	}

	/**
	 * ���º��������
	 * 
	 * @param improvisedHarmony
	 */
	@Override
	public void updateHM(Harmony improvisedHarmony) {
		// TODO Auto-generated method stub
		if (improvisedHarmony.getPerformence() > harmonyMemory.get(0).getPerformence()) {
			harmonyMemory.remove(0);
			harmonyMemory.add(improvisedHarmony);
		}
		Collections.sort(harmonyMemory);
	}

	public void incrementIter() {
		iterator++;
	}
}
