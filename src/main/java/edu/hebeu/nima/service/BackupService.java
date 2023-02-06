package edu.hebeu.nima.service;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;

public interface BackupService {
    String backup(String param) throws IOException;

    String backup() throws IOException;
    void vacuum();
}
