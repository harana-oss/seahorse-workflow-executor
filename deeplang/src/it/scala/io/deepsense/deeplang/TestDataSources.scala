package io.deepsense.deeplang

import java.util.UUID

import org.joda.time.DateTime

import io.deepsense.api.datasourcemanager.model._
import io.deepsense.commons.rest.client.datasources.DatasourceInMemoryClient
import io.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import io.deepsense.deeplang.doperations.readwritedataframe.FilePath
import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme

trait TestDataSources {
  self: TestFiles =>

  lazy val datasourceClient = new DatasourceInMemoryClient(
    someDatasourcesForReading ++ someDatasourcesForWriting
  )

  lazy val someDatasourcesForReading = List(someExternalJsonDatasource, someExternalCsvDatasource)

  lazy val someDatasourcesForWriting = List(someLibraryCsvDatasource, someLibraryJsonDatasource)

  private lazy val someLibraryJsonDatasource = {
    val libraryFilePath = FilePath(FileScheme.Library, "some-file.json")

    new Datasource()
      .id(UUID.randomUUID().toString)
      .accessLevel(AccessLevel.READ)
      .creationDateTime(new DateTime)
      .ownerId(UUID.randomUUID().toString)
      .ownerName("SomeOwner")
      .params(
        new DatasourceParams()
          .name("test-library-json-file")
          .datasourceType(DatasourceType.LIBRARYFILE)
          .libraryFileParams(
            new LibraryFileParams()
              .fileFormat(FileFormat.JSON)
              .libraryPath(libraryFilePath.fullPath)
          )
      )
  }

  private lazy val someLibraryCsvDatasource = {
    val libraryFilePath = FilePath(FileScheme.Library, "some-file.json")

    new Datasource()
      .id(UUID.randomUUID().toString)
      .accessLevel(AccessLevel.READ)
      .creationDateTime(new DateTime)
      .ownerId(UUID.randomUUID().toString)
      .ownerName("SomeOwner")
      .params(
        new DatasourceParams()
          .name("test-library-csv-file")
          .datasourceType(DatasourceType.LIBRARYFILE)
          .libraryFileParams(
            new LibraryFileParams()
              .fileFormat(FileFormat.CSV)
              .libraryPath(libraryFilePath.fullPath)
              .csvFileFormatParams(
                new CsvFileFormatParams()
                  .convert01ToBoolean(true)
                  .includeHeader(true)
                  .separatorType(CsvSeparatorType.COMMA)
              )
          )
      )
  }

  private lazy val someExternalJsonDatasource = {
    val externalFileUrl = testFile(
      new InputFileFormatChoice.Json(),
      FileScheme.HTTPS
    )

    new Datasource()
      .id(UUID.randomUUID().toString)
      .accessLevel(AccessLevel.READ)
      .creationDateTime(new DateTime)
      .ownerId(UUID.randomUUID().toString)
      .ownerName("SomeOwner")
      .params(
        new DatasourceParams()
          .name("test-external-json-file")
          .datasourceType(DatasourceType.EXTERNALFILE)
          .externalFileParams(
            new ExternalFileParams()
              .fileFormat(FileFormat.JSON)
              .url(externalFileUrl)
          )
      )
  }

  private lazy val someExternalCsvDatasource = {
    val externalFileUrl = testFile(
      new InputFileFormatChoice.Csv(),
      FileScheme.HTTPS
    )

    new Datasource()
      .id(UUID.randomUUID().toString)
      .accessLevel(AccessLevel.READ)
      .creationDateTime(new DateTime)
      .ownerId(UUID.randomUUID().toString)
      .ownerName("SomeOwner")
      .params(
        new DatasourceParams()
          .name("test-external-csv-file")
          .datasourceType(DatasourceType.EXTERNALFILE)
          .externalFileParams(
            new ExternalFileParams()
              .fileFormat(FileFormat.CSV)
              .url(externalFileUrl)
              .csvFileFormatParams(
                new CsvFileFormatParams()
                  .separatorType(CsvSeparatorType.COMMA)
                  .convert01ToBoolean(true)
                  .includeHeader(true)
              )
          )
      )
  }

}
