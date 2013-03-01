package net.cammann.results;


public class AveragedResult {

	private int totalIterations;
	private long averageTime;
	private int totalParamCombinations;

	public void setAverageTime(long averageTime) {
		this.averageTime = averageTime;
	}

	public long getAverageTime() {
		return averageTime;
	}

	public void setParamCombinations(int totalParamCombinations) {
		this.totalParamCombinations = totalParamCombinations;
	}

	public int getNumberParamCombinations() {
		return totalParamCombinations;
	}

	public void setIterations(int totalIterations) {
		this.totalIterations = totalIterations;
	}

	public int getNumIterations() {
		return totalIterations;
	}

}
