import com.hp.mercury.ci.jenkins.plugins.downstreamlogs.table.StringProvider

def f = namespace(lib.FormTagLib)

f.entry(title: "Column", field: "column") {
    f.dropdownDescriptorSelector(field: "column")
}
