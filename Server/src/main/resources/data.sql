INSERT INTO public.happy_user_role VALUES ('VISITOR', 'visitor') ON CONFLICT("id") DO UPDATE SET "role" = excluded."role";
INSERT INTO public.happy_user_role VALUES ('ADMIN', 'admin') ON CONFLICT("id") DO UPDATE SET "role" = excluded."role";
INSERT INTO public.happy_user_role VALUES ('MAINRESIDENT', 'mainResident') ON CONFLICT("id") DO UPDATE SET "role" = excluded."role";
INSERT INTO public.happy_user_role VALUES ('NORMALRESIDENT', 'normalResident') ON CONFLICT("id") DO UPDATE SET "role" = excluded."role";
INSERT INTO public.happy_user_role VALUES ('GUARD', 'guard') ON CONFLICT("id") DO UPDATE SET "role" = excluded."role";

INSERT INTO public.happy_permit_status VALUES ('APPROVED', 'approved') ON CONFLICT("id") DO UPDATE SET "status" = excluded."status";
INSERT INTO public.happy_permit_status VALUES ('REJECTED', 'rejected') ON CONFLICT("id") DO UPDATE SET "status" = excluded."status";
INSERT INTO public.happy_permit_status VALUES ('PENDING', 'pending') ON CONFLICT("id") DO UPDATE SET "status" = excluded."status";

INSERT INTO public.happy_permit_type VALUES ('UNIQUE', 'unique') ON CONFLICT("id") DO UPDATE SET "type" = excluded."type";
INSERT INTO public.happy_permit_type VALUES ('MULTIPLE', 'multiple') ON CONFLICT("id") DO UPDATE SET "type" = excluded."type";