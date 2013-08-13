
def l = namespace(lib.LayoutTagLib)
def st = namespace("jelly:stapler")

l.layout() {
    l.side_panel() {
        st.include(page:"sidepanel.jelly", from:my.run, it:my.run)
    }
    l.main_panel() {
        l.h3("warning! you have chosen to stop this build and any downstream builds thereof." +
                "Are you certain?")
        l.form(action:"kill", method:"POST") {
            l.input(type:"button", value:"Back", onclick:"window.history.go(-1)")
            l.input(type:"submit", value:"OK")
        }
    }
}
