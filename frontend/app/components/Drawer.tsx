const Drawer = () => {
    return (
        <div className="drawer-side top-16 z-20 h-[calc(100vh-4rem)] ">
            <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay"></label>
            <ul className="menu bg-base-200 text-base-content min-h-full w-[20%] p-4">
                {/* Sidebar content here */}
                <li>Sidebar Item 1</li>
                <li>Sidebar Item 2</li>
            </ul>
        </div>
    )
}

export default Drawer;