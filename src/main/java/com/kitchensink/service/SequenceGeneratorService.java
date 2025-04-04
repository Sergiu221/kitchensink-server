package com.kitchensink.service;

import com.kitchensink.model.Sequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    public SequenceGeneratorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public long generateSequence(String seqName) {
        Sequence sequence = mongoTemplate.findAndModify(
                Query.query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options()
                        .returnNew(true)
                        .upsert(true),
                Sequence.class
        );

        return (sequence != null) ? sequence.getSeq() : 1L;
    }
}

