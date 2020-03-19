package Common;

import java.util.stream.IntStream;

import lombok.Value;

/**
 * �Ա���ȡֵ��Χ
 * 
 * @author hba
 *
 */

public class Range {
	/**
	 * ά��
	 */
	private int dim;
	/**
	 * ȡֵ����
	 */
	private double[] high;
	/**
	 * ȡֵ����
	 */
	private double[] low;
	/**
	 * ȡֵ��Χ
	 */
	private double[] scale;

	public Range(int dim, double[] high, double[] low) {
		super();
		this.dim = dim;
		this.high = high;
		this.low = low;
	}

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public double[] getHigh() {
		return high;
	}

	public void setHigh(double[] high) {
		this.high = high;
	}

	public double[] getLow() {
		return low;
	}

	public void setLow(double[] low) {
		this.low = low;
	}

	public double[] getScale() {
		this.scale = IntStream.range(0, high.length).mapToDouble(i -> high[i] - low[i]).toArray();
		return scale;
	}

	public void setScale(double[] scale) {
		this.scale = scale;
	}
}
