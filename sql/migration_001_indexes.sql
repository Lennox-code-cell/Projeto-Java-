-- Optional, non-destructive index for the report's date-range filter.
-- Apply once to an existing database after taking a backup.
CREATE INDEX idx_emprestimo_data_emprestimo
    ON emprestimo (data_emprestimo);
