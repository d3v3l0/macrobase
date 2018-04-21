package edu.stanford.futuredata.macrobase.analysis.summary.aplinear;

import edu.stanford.futuredata.macrobase.analysis.summary.util.qualitymetrics.*;
import edu.stanford.futuredata.macrobase.datamodel.DataFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Summarizer that works over both cube and row-based labeled ratio-based outlier summarization.
 */
public class APLSupportSummarizer extends APLSummarizer {

    private Logger log = LoggerFactory.getLogger("APLSupportSummarizer");
    private String countColumn = null;

    @Override
    public List<String> getAggregateNames() {
        return Arrays.asList("Count");
    }

    @Override
    public AggregationOp[] getAggregationOps() {
        AggregationOp[] curOps = {AggregationOp.SUM};
        return curOps;
    }

    @Override
    public int[][] getEncoded(List<String[]> columns, DataFrame input) {
        return encoder.encodeAttributesAsArray(columns);
    }

    @Override
    public double[][] getAggregateColumns(DataFrame input) {
        double[] countCol = processCountCol(input, countColumn, input.getNumRows());

        double[][] aggregateColumns = new double[1][];
        aggregateColumns[0] = countCol;

        return aggregateColumns;
    }

    @Override
    public List<QualityMetric> getQualityMetricList() {
        List<QualityMetric> qualityMetricList = new ArrayList<>();
        qualityMetricList.add(
            new SupportQualityMetric(0, fullNumOutliers)
        );
        return qualityMetricList;
    }

    @Override
    public List<Double> getThresholds() {
        return Arrays.asList(minOutlierSupport);
    }

    @Override
    public double getNumberOutliers(double[][] aggregates) {
        double count = 0.0;
        double[] outlierCount = aggregates[0];
        for (int i = 0; i < outlierCount.length; i++) {
            count += outlierCount[i];
        }
        return count;
    }

    public String getCountColumn() {
        return countColumn;
    }

    public void setCountColumn(String countColumn) {
        this.countColumn = countColumn;
    }
}
