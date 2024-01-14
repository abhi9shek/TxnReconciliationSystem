package com.example.TxnReconciliation.service.fileFactory;

import com.example.TxnReconciliation.service.fileReader.IFileReader;
import com.example.TxnReconciliation.service.fileWriter.IFileWriter;

public interface IFileFactory {

    IFileReader createFileReader();
    IFileWriter createFileWriter();
}
