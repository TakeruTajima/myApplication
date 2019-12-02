package com.mr2.zaiko.Infra;

import android.content.Context;

import com.mr2.zaiko.Domain.Company.CompanyRepository;
import com.mr2.zaiko.Domain.Company.CompanyRepositoryImpl;
import com.mr2.zaiko.Domain.UnitType.UnitTypeRepository;
import com.mr2.zaiko.Domain.UnitType.UnitTypeRepositoryImpl;

public class RepositoryService {

    public static UnitTypeRepository getUnitTypeRepository(Context context){
        return new UnitTypeRepositoryImpl(context);
    }

    public static CompanyRepository getCompanyRepository(Context context){
        return new CompanyRepositoryImpl(context);
    }
}
