package classification;

import java.util.Map;
import java.util.HashMap;

public class ConfusionMatrix {
        Map<EmailClass, Map<EmailClass, Integer>> confusion = new HashMap<EmailClass, Map<EmailClass, Integer>>();

        public ConfusionMatrix() {
            confusion.put(EmailClass.Spam, new HashMap<EmailClass, Integer>());
            confusion.get(EmailClass.Spam).put(EmailClass.Spam, 0);
            confusion.get(EmailClass.Spam).put(EmailClass.Ham, 0);
            confusion.put(EmailClass.Ham, new HashMap<EmailClass, Integer>());
            confusion.get(EmailClass.Ham).put(EmailClass.Spam, 0);
            confusion.get(EmailClass.Ham).put(EmailClass.Ham, 0);
        }

        public void add(EmailClass actualClass, EmailClass emailClass) {
                confusion.get(actualClass).put(emailClass, confusion.get(actualClass).get(emailClass) + 1);
        }

        public int getTotal() {
            return getValue(EmailClass.Ham, EmailClass.Ham) + getValue(EmailClass.Spam, EmailClass.Spam) +
                   getValue(EmailClass.Spam, EmailClass.Ham) + getValue(EmailClass.Ham, EmailClass.Spam);
        }

        public int getValue(EmailClass actual, EmailClass predicted) {
            return confusion.get(actual).get(predicted);
        }

        // Assuming Spam=positive class Ham=negative class
        public int getTruePositive() {
            return getValue(EmailClass.Spam, EmailClass.Spam);
        }

        public int getTrueNegative() {
            return getValue(EmailClass.Ham, EmailClass.Ham);
        }

        public int getFalsePositive() {
            return getValue(EmailClass.Ham, EmailClass.Spam);
        }

        public int getFalseNegative() {
            return getValue(EmailClass.Spam, EmailClass.Ham);
        }

        public double getAccuracy() {
            int correctlyPredicted = getValue(EmailClass.Spam, EmailClass.Spam) + getValue(EmailClass.Ham, EmailClass.Ham);
            return ((double) correctlyPredicted) / (double) getTotal();
        }

        // i.e. True Positive Rate
        public double getRecall() {
            int truePositive = getTruePositive();
            int totalPositive = getTruePositive() + getFalseNegative();

            return ((double) truePositive / (double) totalPositive);
        }

        // i.e. True Negative Rate
        public double getNegativeRecall() {
            int trueNegative = getTrueNegative();
            int totalNegative = getTrueNegative() + getFalsePositive();

            return ((double) trueNegative / (double) totalNegative);
        }

        // i.e. Confidence
        public double getPrecision() {
            int truePositive = getTruePositive();
            int falsePositive = getFalsePositive();

            return ((double) truePositive / (double) (truePositive + falsePositive));
        }

        @Override
        public String toString() {
            return          "   |   Spam |    Ham |\n" +
                            "---|--------|--------|\n" +
              String.format(" S | % 6d | % 6d |\n", getValue(EmailClass.Spam, EmailClass.Spam), getValue(EmailClass.Spam, EmailClass.Ham)) +
              String.format(" H | % 6d | % 6d |\n", getValue(EmailClass.Ham, EmailClass.Spam), getValue(EmailClass.Ham, EmailClass.Ham));
        }
}
