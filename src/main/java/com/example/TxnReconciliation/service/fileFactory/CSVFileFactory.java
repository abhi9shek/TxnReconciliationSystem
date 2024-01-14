package com.example.TxnReconciliation.service.fileFactory;

import com.example.TxnReconciliation.service.fileReader.CSVFileReader;
import com.example.TxnReconciliation.service.fileReader.IFileReader;
import com.example.TxnReconciliation.service.fileWriter.CSVFileWriter;
import com.example.TxnReconciliation.service.fileWriter.IFileWriter;

public class CSVFileFactory implements IFileFactory {
    @Override
    public IFileReader createFileReader() {
        return new CSVFileReader();
    }

    @Override
    public IFileWriter createFileWriter() {
        return new CSVFileWriter();
    }
}
