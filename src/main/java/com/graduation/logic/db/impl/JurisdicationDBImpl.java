package com.graduation.logic.db.impl;

import com.graduation.logic.db.HbaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** Created by kuirons on 18-4-7 */
@Repository
public class JurisdicationDBImpl {
  @Autowired HbaseManager hbaseManager;
}
