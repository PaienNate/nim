package edu.hebeu.nima.service;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.sql.SQLException;

public interface BackupService {
    String backup(String param) throws IOException, SQLException;

    String backup() throws IOException, SQLException;
    void vacuum();
}
