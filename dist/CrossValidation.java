package spamfilter;

import classification.ConfusionMatrix;
import classification.EmailClass;
import classification.EmailClassifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CrossValidation {
    protected ArrayList<File> files;
    protected EmailClassifier classifier;
    protected ConfusionMatrix confusionMatrix;
    protected List<ConfusionMatrix> confusionMatrices;

    public CrossValidation(String directory, EmailClassifier classifier) {
        this.classifier = classifier;

        File trainingDirectory = new File(directory);
        files = new ArrayList<File>(Arrays.asList(trainingDirectory.listFiles(new SpamHamFileFilter())));
    }

    public double getStdDev() {
        double mean = 0.0;
        for (ConfusionMatrix cm : confusionMatrices) {
            mean += cm.getAccuracy();
        }
        mean /= confusionMatrices.size();
        double o2 = 0.0;
        for (ConfusionMatrix cm : confusionMatrices) {
            o2 += (cm.getAccuracy() - mean) * (cm.getAccuracy() - mean);
        }
        return Math.sqrt(o2 / (confusionMatrices.size() - 1));
    }

    public void fold(int folds, double lowerPercentile, double upperPercentile) throws IOException {
        // Shuffle the list, so that we get each time other results
        Collections.shuffle(files);

        // Split into folds
        List<List<File>> fileFolds = new ArrayList<List<File>>(folds);
        int foldLength = files.size() / folds;
        for (int i = 0; i < folds; i++) {
            int end = (i == (folds - 1)) ? files.size() : ((i+1) * foldLength);
            fileFolds.add(files.subList(i * foldLength, end));
        }

        // For each combination spamfilter and test
        confusionMatrix = new ConfusionMatrix();
        confusionMatrices = new ArrayList<ConfusionMatrix>();
        for (int i = 0; i < folds; i++) {
            // Gather training data
            List<File> train = new ArrayList<File>();
            for (int j = 0; j < folds; j++) {
                if (j != i) {
                    train.addAll(fileFolds.get(j));
                }
            }

            classifier.train(train, lowerPercentile, upperPercentile);
            
            ConfusionMatrix cm = new ConfusionMatrix();
            for(File trainingFile : fileFolds.get(i)) {
                EmailClass actualClass = trainingFile.getName().contains("ham")? EmailClass.Ham : EmailClass.Spam;
                EmailClass emailClass = classifier.classify(trainingFile);

                confusionMatrix.add(actualClass, emailClass);
                cm.add(actualClass, emailClass);
            }
            confusionMatrices.add(cm);
        }
    }

    public ConfusionMatrix getCombinedConfusion() {
        return confusionMatrix;
    }
}
