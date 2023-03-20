package com.library.mapper;

import com.library.domain.Loan;
import com.library.domain.User;
import com.library.dto.LoanDTO;
import com.library.dto.LoanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;



@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(source = "user",target = "userId",qualifiedByName = "convertUser")
    LoanDTO loanToLonaDTO(Loan loan);

    @Named("convertUser")
    public static Long convertUser(User user){
        return user.getId();
    }

    @Mapping(source="user", target="userId", qualifiedByName = "getUserId")
    LoanResponse loanDTOToLoanResponse(Loan loan);

    @Named("getUserId")
    public static Long getUserId(User user){
        return user.getId();
    }
}