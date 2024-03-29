USE [doan1]
GO
/****** Object:  Table [dbo].[account]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[account](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[fullname] [varchar](255) NOT NULL,
	[role] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[folder]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[folder](
	[path] [varchar](255) NOT NULL,
	[foldername] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[path] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[managementfolder]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[managementfolder](
	[userid] [int] NOT NULL,
	[folderid] [varchar](255) NOT NULL,
	[permission] [varchar](4) NOT NULL,
 CONSTRAINT [pk_managementfolder] PRIMARY KEY CLUSTERED 
(
	[userid] ASC,
	[folderid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[managementfolder]  WITH CHECK ADD  CONSTRAINT [fk_managementfolder_account] FOREIGN KEY([userid])
REFERENCES [dbo].[account] ([id])
GO
ALTER TABLE [dbo].[managementfolder] CHECK CONSTRAINT [fk_managementfolder_account]
GO
ALTER TABLE [dbo].[managementfolder]  WITH CHECK ADD  CONSTRAINT [fk_managementfolder_folder] FOREIGN KEY([folderid])
REFERENCES [dbo].[folder] ([path])
GO
ALTER TABLE [dbo].[managementfolder] CHECK CONSTRAINT [fk_managementfolder_folder]
GO
/****** Object:  StoredProcedure [dbo].[spInitPer]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[spInitPer]
	@uid int
as
	declare @path varchar(255); 
	declare Folder_Cursor CURSOR
	for (select path from folder)
	open Folder_Cursor; 
	fetch next from Folder_Cursor into @path;
	while @@FETCH_STATUS <> -1 
		begin 
			insert into managementfolder values (@uid, @path, '0000');
			fetch next from Folder_Cursor into @path;
		end;
	close Folder_Cursor; 
	deallocate Folder_Cursor;

GO
/****** Object:  StoredProcedure [dbo].[spWriteFolder]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[spWriteFolder]
	@path varchar(255),
	@name varchar(255)
as
	if not exists (select * from folder where path = @path)
		insert into folder values (@path, @name);

GO
/****** Object:  StoredProcedure [dbo].[spWritePer]    Script Date: 12/19/2019 11:17:29 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[spWritePer]
	@path varchar (255),
	@id int,
	@per varchar(4)
as
	if exists (select * from managementfolder where userid = @id and folderid = @path) and @per is not null
		update managementfolder set permission = @per where userid = @id and folderid = @path;
	else insert into managementfolder values (@id, @path, @per);

GO
