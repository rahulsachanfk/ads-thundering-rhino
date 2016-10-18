var jsonData = {};
function makeajax(clusterName, url, item) {
    $.getJSON("/services/package-versions?url=" + url)
        .done(function (json) {
            updateJsonData(json, clusterName, item);
            var html = '<td>' + updateClusterHTML(clusterName) + '</td>';
            $('#' + clusterName).html(html);
        })
        .fail(function (xhr, textStatus, error) {
            alert("Failed");
        });
}

function updateJsonData(response, clusterName, item) {
    var clusters = [];
    if (jsonData.clusters) {
        clusters = jsonData.clusters;
    } else {
        jsonData.clusters = clusters;
    }
    var cluster = {};
    var clusterFound = false;
    if (clusters.length > 0) {
        for (var i = 0; i < clusters.length; i++) {
            if (clusters[i].name == clusterName) {
                cluster = clusters[i];
                clusterFound = true;
                break;
            }
        }
    }
    if (!clusterFound) {
        cluster.name = clusterName;
        cluster.packages = [];
        clusters.push(cluster);
    }

    for (key in response) {
        var pkg = {};
        var pkgFound = false;
        for (var i = 0; i < cluster.packages.length; i++) {
            if (cluster.packages[i].name == key) {
                pkg = cluster.packages[i];
                pkgFound = true;
                break;
            }
        }
        if (!pkgFound) {
            pkg.name = key;
            pkg.versions = [];
            cluster.packages.push(pkg);
        }
        var versionInfo = response[key];
        var version = {};
        var versionFound = false;
        for (var i = 0; i < pkg.versions.length; i++) {
            if (pkg.versions[i].name == versionInfo) {
                version = pkg.versions[i];
                versionFound = true;
                break;
            }
        }

        if (!versionFound) {
            version.name = versionInfo;
            version.hosts = [];
            pkg.versions.push(version);
        }

        version.hosts.push(item);
    }
}

function updateClusterHTML(clusterName) {
    var html = '';
    for (var i = 0; i < jsonData.clusters.length; i++) {
        var cluster = jsonData.clusters[i];
        if (cluster.name == clusterName) {
            html = updateHTML(clusterName, cluster);
            break;
        }
    }

    return html;
}

function updateHTML(clusterName, cluster) {
    var html = '<table class="package"><tbody><tr>';
    for (var i = 0; i < cluster.packages.length; i++) {
        var pkg = cluster.packages[i];
        html += '<th colspan="2">' + pkg.name + '</th></tr>';
        for (var j = 0; j < pkg.versions.length; j++) {
            var version = pkg.versions[j];
            html += '<tr><td class="head">' + version.name + '</td><td>';
            for (var k = 0; k < version.hosts.length; k++) {
                var host = version.hosts[k];
                html += '<span class="server">' + host + '</span>';
            }

            html += '</td></tr>';
        }

        html += '</tr>';
    }
    html += '</tbody></table>';

    return html;
}

function makeURL(item, host, url, port) {
    var firstDot = host.indexOf(".");
    var len = host.length;
    return "http://" + host.substring(0, firstDot) + item + host.substring(firstDot, len) + ":" + port + "/" + url;
}

function getItem(host) {
    var firstDot = host.indexOf(".");
    var len = host.length;

    return host.substring(0, firstDot);
}

function createDashboard() {
    var obj = getClusters(input);
    var clusters = obj.clusters[0];
    var oogway_cluster_externalapi = clusters.oogway[0].externalapi[0];
    var oogway_cluster_internalapi = clusters.oogway[0].internalapi[0];
    var revive_cluster_admin = clusters.revive[0].admin[0];
    var revive_cluster_adserve = clusters.revive[0].adserve[0];
    var revive_cluster_native = clusters.revive[0].native[0];
    var revive_cluster_preprod = clusters.revive[0].preprod[0];
    var revive_cluster_maintanence = clusters.revive[0].maintanence[0];

    var item = getItem(oogway_cluster_externalapi.host);
    for (var i = 0; i < oogway_cluster_externalapi.carray.length; i++) {
        url = makeURL(oogway_cluster_externalapi.carray[i], oogway_cluster_externalapi.host, oogway_cluster_externalapi.url, oogway_cluster_externalapi.port);
        makeajax("excluster", url, item + oogway_cluster_externalapi.carray[i]);
    }

    item = getItem(oogway_cluster_internalapi.host);
    for (var i = 0; i < oogway_cluster_internalapi.carray.length; i++) {
        url = makeURL(oogway_cluster_internalapi.carray[i], oogway_cluster_internalapi.host, oogway_cluster_internalapi.url, oogway_cluster_internalapi.port);
        makeajax("incluster", url, item + oogway_cluster_internalapi.carray[i]);
    }

    item = getItem(revive_cluster_adserve.host);
    for (var i = 0; i < revive_cluster_adserve.carray.length; i++) {
        url = makeURL(revive_cluster_adserve.carray[i], revive_cluster_adserve.host, revive_cluster_adserve.url, revive_cluster_adserve.port);
        makeajax("adservcluster", url, item + revive_cluster_adserve.carray[i]);
    }

    item = getItem(revive_cluster_native.host);
    for (var i = 0; i < revive_cluster_native.carray.length; i++) {
        url = makeURL(revive_cluster_native.carray[i], revive_cluster_native.host, revive_cluster_native.url, revive_cluster_native.port);
        makeajax("nativecluster", url, item + revive_cluster_native.carray[i]);
    }

    item = getItem(revive_cluster_admin.host);
    for (var i = 0; i < revive_cluster_admin.carray.length; i++) {
        url = makeURL(revive_cluster_admin.carray[i], revive_cluster_admin.host, revive_cluster_admin.url, revive_cluster_admin.port);
        makeajax("admincluster", url, item + revive_cluster_admin.carray[i]);
    }

    item = getItem(revive_cluster_maintanence.host);
    for (var i = 0; i < revive_cluster_maintanence.carray.length; i++) {
        url = makeURL(revive_cluster_maintanence.carray[i], revive_cluster_maintanence.host, revive_cluster_maintanence.url, revive_cluster_maintanence.port);
        makeajax("maintenancecluster", url, item + revive_cluster_maintanence.carray[i]);
    }
}

$(document).ready(createDashboard);

setTimeout(function () {
    window.location.reload(1);
}, 60000);
