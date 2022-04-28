package org.eclipse.dataspaceconnector.mvd;

import org.eclipse.dataspaceconnector.catalog.spi.FederatedCacheNode;
import org.eclipse.dataspaceconnector.catalog.spi.FederatedCacheNodeDirectory;
import org.eclipse.dataspaceconnector.spi.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RefreshCatalogServiceTest {

    @Test
    void saveNodeEntries() {
        var directory = mock(FederatedCacheNodeDirectory.class);
        var sampleFile = getClass().getClassLoader().getResource("24-node1.json");
        assertThat(sampleFile).isNotNull();
        var nodeJsonDir = Path.of(sampleFile.getPath()).getParent();
        var nodeJsonPrefix = "24-";
        var monitor = new ConsoleMonitor();
        var typeManager = new TypeManager();
        var extension = new RefreshCatalogService(directory, nodeJsonDir, nodeJsonPrefix, monitor, typeManager);

        when(directory.getAll()).thenReturn(List.of(new FederatedCacheNode("node24-2", "", List.of(""))));

        extension.saveNodeEntries();

        verify(directory, times(1)).insert(argThat(n -> "node24-1".equals(n.getName())));
        verify(directory, times(0)).insert(argThat(n -> "node24-2".equals(n.getName())));
        verify(directory, times(1)).insert(argThat(n -> "node24-3".equals(n.getName())));
    }
}