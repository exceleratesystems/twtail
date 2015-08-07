package com.excelsys.twtail;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import twitter4j.*;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TwitterStream stream;

    private final SnsPublisher snsPublisher;

    public Main(String[] args) throws Exception {
        this.stream = TwitterStreamFactory.getSingleton();

        if (0 != args.length) {
            String topicArn = args[0];

            snsPublisher = new SnsPublisher(topicArn);
        } else {
            snsPublisher = null;
        }

        this.stream.addListener(new TwtailStatusAdapter());
    }

    class SnsPublisher {
        private final AmazonSNSClient snsClient;

        private final String topicArn;

        public SnsPublisher(String topicArn) throws Exception {
            this.topicArn = topicArn;
            this.snsClient = new AmazonSNSClient();
        }

        public void onStatus(Status status) {
            try {
                String statusAsString = OBJECT_MAPPER.writeValueAsString(status);

                PublishRequest req = new PublishRequest().//
                        withMessage(statusAsString).//
                        withSubject("" + status.getId()).//
                        withTopicArn(topicArn);

                snsClient.publish(req);
            } catch (Exception ex) {
                dumpError(ex);
            }
        }
    }

    class TwtailStatusAdapter extends UserStreamAdapter {
        @Override
        public void onStatus(Status status) {
            dump(status);

            if (null != snsPublisher)
                snsPublisher.onStatus(status);
        }

        @Override
        public void onException(Exception ex) {
            dumpError(ex);
        }
    }

    public void dump(Status status) {
        try {
            System.out.println(OBJECT_MAPPER.writeValueAsString(status));
        } catch (Exception ex) {
            dumpError(ex);
        }
    }

    public void dumpError(Exception exc) {
        exc.printStackTrace(System.err);
    }

    public void execute() throws Exception {
        stream.user();
    }

    public static void main(String[] args) throws Exception {
        new Main(args).execute();
    }
}
