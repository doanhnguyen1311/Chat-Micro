import { Outlet } from "react-router-dom"
import MessengerSidebar from "../components/Messenger"
import Header from "../components/Header"
import MiniSidebar from "../components/MiniSidebar"

const AuthLayout = () => {
    return (
        <div className='main-layout h-100vh'>
            <div className="d-flex h-100">
                <MiniSidebar />
                <div className="bg-white h-100vh w-100 hide-scrollbar" style={{overflowY: 'auto'}}>
                    <Header />
                    <div>
                        {/* Sub Header */}
                        <div className="d-flex">
                            {/* Photo */}
                            <Outlet />
                            {/* Recent Activity */}
                        </div>
                    </div>
                </div>
                <MessengerSidebar />
            </div>
        </div>
    )
}

export { AuthLayout }