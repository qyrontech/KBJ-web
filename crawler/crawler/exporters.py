from scrapy.exporters import CsvItemExporter, JsonItemExporter


class JsonFileItemExporter(JsonItemExporter):
    def __init__(self, file, **kwargs):
        super(JsonFileItemExporter, self).__init__(file, encoding='utf-8')

    def start_exporting(self):
        self.file.write(b'{\"data\": [\n')

    def finish_exporting(self):
        self.file.write(b'\n]}')


class CsvFileItemExporter(CsvItemExporter):
    def __init__(self, file, **kwargs):
        super(CsvFileItemExporter, self).__init__(file, encoding='utf-8')
