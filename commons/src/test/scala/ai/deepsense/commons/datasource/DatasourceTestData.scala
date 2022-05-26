package ai.deepsense.commons.datasource

import java.util.UUID

import org.joda.time.DateTime

import ai.deepsense.api.datasourcemanager.model.AccessLevel
import ai.deepsense.api.datasourcemanager.model.DatasourceParams
import ai.deepsense.api.datasourcemanager.model.DatasourceType
import ai.deepsense.api.datasourcemanager.model.Visibility
import ai.deepsense.api.datasourcemanager.model._

object DatasourceTestData {

  def multicharSeparatorLibraryCsvDatasource: Datasource = {
    val ds                = new Datasource
    val libraryFileParams = new LibraryFileParams
    libraryFileParams.setLibraryPath("some_path")
    libraryFileParams.setFileFormat(FileFormat.CSV)
    val csvType           = new CsvFileFormatParams
    csvType.setConvert01ToBoolean(false)
    csvType.setIncludeHeader(false)
    csvType.setSeparatorType(CsvSeparatorType.CUSTOM)
    csvType.setCustomSeparator(",,")
    libraryFileParams.setCsvFileFormatParams(csvType)

    val params = new DatasourceParams
    params.setDatasourceType(DatasourceType.LIBRARYFILE)
    params.setLibraryFileParams(libraryFileParams)
    params.setName("name")
    params.setVisibility(Visibility.PUBLICVISIBILITY)

    ds.setCreationDateTime(new DateTime)
    ds.setParams(params)
    ds.setId(UUID.randomUUID.toString)
    ds.setCreationDateTime(new DateTime())
    ds.setAccessLevel(AccessLevel.WRITEREAD)
    ds.setOwnerId("abcd")
    ds.setOwnerName("owner_name")
    ds
  }

}
