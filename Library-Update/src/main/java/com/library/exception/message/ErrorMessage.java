package com.library.exception.message;


public class ErrorMessage {

    public final static String RESOURCE_NOT_FOUND_EXCEPTION="Resource with id %s not found";

    public final static String ROL_NOT_FOUND_EXCEPTION="Role: %s not found";

    public final static String USER_NOT_FOUND_EXCEPTION="User: %s not found";

    public final static String JWT_TOKEN_ERROR_MESSAGE="Jwt token validation error : %s";

    public final static String EMAIL_CONFLICT_EXCEPTION="E mail already registered : %s";

    public final static String PRINCIPAL_FOUND_MESSAGE = "User not found";

    public final static String NOT_PERMITED_METOD_MESSAGE = "You dont have permission to change this data";

    public final static String IMAGE_NOT_FOUND_EXCEPTION="Image with id %s not found";

    public final static String LOAN_NOT_FOUND_EXCEPTION="Loan with id %s not found";

    public final static String EXCEL_REPORT_ERROR_MESSAGE = "Error occured while generating excel report";

    public final static String UNAUTHORIZED_ERROR_MESSAGE = "You do not have the authority";

    public final static String BOOK_NOT_FOUND_EXCEPTION="Book: %s not found";

    public final static String USER_HAS_LOAN_EXCEPTION="User has active loan";

    public final static String CATEGORY_NOT_FOUND_EXCEPTION="Category: %s not found";

    public final static String AUTHOR_HAS_BOOK_EXCEPTION="Author has active book";

    public final static String PUBLISHER_NOT_FOUND_EXCEPTION="Publisher: %s not found";

    public final static String LOANABLE_EXCEPTION="This Book is not available";

    public final static String RETURNDATE_EXCEPTION="First you have to bring the expired book";
}