package com.lms.service;
import com.lms.dtos.ReturnedBookDTO;

public interface ReturnService {
    ReturnedBookDTO returnBook(Long issueId);
}