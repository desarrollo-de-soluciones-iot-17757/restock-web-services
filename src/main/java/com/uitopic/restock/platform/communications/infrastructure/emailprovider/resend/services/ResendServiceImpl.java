package com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.services;

import com.uitopic.restock.platform.communications.infrastructure.emailprovider.resend.ResendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ResendServiceImpl implements ResendService {

    @Override
    public void sendEmail(String to, String subject, List<Pair<String, String>> htmlVariables, String templateId) {

    }
}
